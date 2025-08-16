package dev.zeddevstuff.mead.core;

import com.mojang.logging.LogUtils;
import dev.zeddevstuff.mead.core.elements.RootElement;
import dev.zeddevstuff.mead.core.elements.RectElement;
import dev.zeddevstuff.mead.core.elements.TextElement;
import dev.zeddevstuff.mead.core.elements.flow.IfElement;
import dev.zeddevstuff.mead.core.elements.interactive.ButtonElement;
import dev.zeddevstuff.mead.core.elements.parsing.ImportElement;
import dev.zeddevstuff.mead.core.elements.parsing.StyleElement;
import dev.zeddevstuff.mead.core.parsing.MeadParser;
import dev.zeddevstuff.mead.core.parsing.MeadStyleSheetsParser;
import dev.zeddevstuff.mead.core.styling.*;
import dev.zeddevstuff.mead.utils.NullUtils;
import org.slf4j.Logger;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;
import java.util.jar.JarFile;
import java.util.regex.Pattern;

/**
 * The centralized interface to Mead. It is recommended for you to use this instead of directly using Mead classes.
 */
public class MeadContext
{
    private final Logger logger = LogUtils.getLogger();
    private final UUID key = UUID.randomUUID();
    public final Registry<MeadParser.IMeadElementFactory> elementFactories = new Registry<>(key);
    public final Registry<IMeadStylePropertyApplier> stylePropertyAppliers = new Registry<>(key);
    private final String modid;
    private final Class<?> modClass;

    private final HashMap<String, IntermediateDOM> intermediaryDOMs = new HashMap<>();
    public Optional<IntermediateDOM> getIntermediaryDOM(String path)
    {
        return Optional.ofNullable(intermediaryDOMs.get(path));
    }
    private final HashMap<String, MeadStyle> styleSheets = new HashMap<>();
    public Optional<MeadStyle> getStyleSheet(String path)
    {
        return Optional.ofNullable(styleSheets.get(path));
    }

    /**
     * Initializes the Mead context with the given mod ID and mod class.
     * @param modid Your mod ID. Make sure it is the same as your "assets" namespace.
     * @throws IllegalArgumentException If the mod class is null or does not appear to be a mod.
     */
    public MeadContext(String modid, Class<?> modClass) throws IllegalArgumentException
    {
        checkModClass(modClass);
        this.modid = modid;
        this.modClass = modClass;
        registerDefaults(false);
        loadMeadDocuments();
        loadStyleSheets();
    }
    /**
     * Initializes the Mead context with the given mod ID and mod class.
     * @param modid Your mod ID. Make sure it is the same as your "assets" namespace.
     * @param htmlLike When true, will rename some elements to have their tag match the closest HTML element
     * @throws IllegalArgumentException If the mod class is null or does not appear to be a mod.
     */
    public MeadContext(String modid, Class<?> modClass, boolean htmlLike) throws IllegalArgumentException
    {
        checkModClass(modClass);
        this.modid = modid;
        this.modClass = modClass;
        registerDefaults(htmlLike);
        loadMeadDocuments();
        loadStyleSheets();
    }

    private void registerDefaults(boolean htmlLike)
    {
        registerDefaultFactories(htmlLike);
        registerDefaultStylePropertyAppliers();
        registerMeadStyle();
    }
    private void registerDefaultFactories(boolean htmlLike)
    {
        if(!htmlLike)
        {
            elementFactories.register("Mead", RootElement::new);
            // Parsing
            elementFactories.register("Import", ImportElement::new);
            elementFactories.register("Style", StyleElement::new);
            // Elements
            elementFactories.register("Rect", RectElement::new);
            elementFactories.register("Text", TextElement::new);
            // Interactive elements
            elementFactories.register("Button", ButtonElement::new);
            // Flow control elements
            elementFactories.register("If", IfElement::new);
        }
        else
        {
            elementFactories.register("html", RootElement::new);
            // Parsing
            elementFactories.register("import", ImportElement::new);
            elementFactories.register("style", StyleElement::new);
            // Elements
            elementFactories.register("div", RectElement::new);
            elementFactories.register("p", TextElement::new);
            // Interactive elements
            elementFactories.register("button", ButtonElement::new);
            // Flow control elements
            elementFactories.register("if", IfElement::new);
        }
    }
    private void registerDefaultStylePropertyAppliers()
    {
        stylePropertyAppliers.register("Layout", new LayoutStylePropertyApplier());
        stylePropertyAppliers.register("Color", new ColorStylePropertyApplier());
        stylePropertyAppliers.register("Text", new TextStylePropertyApplier());
    }
    private void registerMeadStyle()
    {
        MeadStyle style = null;
    }
    private static final Pattern tempPattern = Pattern.compile(".+\\.jar");
    private void loadMeadDocuments()
    {
        try
        {
            NullUtils.ifNotNull(modClass.getProtectionDomain().getCodeSource(), codeSource ->
            {
                NullUtils.ifNotNull(codeSource.getLocation(), location -> {
                    var matcher = tempPattern.matcher(location.getPath());
                    if(matcher.find())
                    {
                        try (JarFile jarFile = new JarFile(matcher.group()))
                        {
                            jarFile.stream()
                                .filter(entry -> entry.getName().startsWith("assets/" + modid + "/ui/") && entry.getName().endsWith(".mead"))
                                .forEach(entry -> {
                                    try (InputStream inputStream = jarFile.getInputStream(entry))
                                    {
                                        String relativePath = entry.getName().replace("assets/" + modid + "/ui/", "");
                                        String content = new String(inputStream.readAllBytes());
                                        MeadParser.parse(content)
                                            .ifPresent(intermediary -> intermediaryDOMs.put(relativePath, intermediary));
                                    } catch (Exception ignored) {}
                                });
                        }
                        catch (Exception ignored) {}
                    }
                });
            });
        }
        catch(Exception e)
        {
            logger.error("Failed to load mead documents for mod {}", modid, e);
        }
        var size = intermediaryDOMs.size();
        if(size == 1)
            logger.info("Loaded 1 mead document for mod {}", modid);
        else
            logger.info("Loaded {} mead documents for mod {}", intermediaryDOMs.size(), modid);
    }
    private void loadStyleSheets()
    {
        try
        {
            NullUtils.ifNotNull(modClass.getProtectionDomain().getCodeSource(), codeSource ->
                {
                    NullUtils.ifNotNull(codeSource.getLocation(), location -> {
                        var matcher = tempPattern.matcher(location.getPath());
                        if(matcher.find())
                        {
                            try (JarFile jarFile = new JarFile(matcher.group()))
                            {
                                jarFile.stream()
                                    .filter(entry -> entry.getName().startsWith("assets/" + modid + "/ui/") && entry.getName().endsWith(".mss"))
                                    .forEach(entry -> {
                                        try (InputStream inputStream = jarFile.getInputStream(entry))
                                        {
                                            String relativePath = entry.getName().replace("assets/" + modid + "/ui/", "");
                                            String content = new String(inputStream.readAllBytes());
                                            var result = MeadStyleSheetsParser.parse(this, content);
                                            result.ifPresent(style -> styleSheets.put(relativePath, style));
                                        } catch (Exception ignored) {}
                                    });
                            }
                            catch (Exception ignored) {}
                        }
                    });
                });
        }
        catch(Exception e)
        {
            logger.error("Failed to load style sheets for mod {}", modid, e);
        }
        var size = styleSheets.size();
        if(size == 1)
            logger.info("Loaded 1 stylesheet for mod {}", modid);
        else
            logger.info("Loaded {} stylesheets for mod {}", styleSheets.size(), modid);
    }

    private void checkModClass(Class<?> modClass)
    {
        if(modClass == null)
            throw new IllegalArgumentException("Mod class cannot be null");
        var classLoader = modClass.getClassLoader();
        if(classLoader.getResource("META-INF/neoforge.mods.toml") != null)
            return;
        if(classLoader.getResource("fabric.mod.json") != null)
            return;
        if(classLoader.getResource("META-INF/mods.toml") != null)
            return;
        throw new IllegalArgumentException("Class " + modClass.getName() + " does not appear to be a mod.");
    }
}
