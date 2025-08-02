package dev.zeddevstuff.mead.core;

import com.llamalad7.mixinextras.utils.MixinExtrasLogger;
import com.mojang.logging.LogUtils;
import dev.zeddevstuff.mead.core.elements.Element;
import dev.zeddevstuff.mead.core.elements.RectElement;
import dev.zeddevstuff.mead.core.elements.TextElement;
import dev.zeddevstuff.mead.core.elements.flow.IfElement;
import dev.zeddevstuff.mead.core.elements.interactive.ButtonElement;
import dev.zeddevstuff.mead.core.elements.parsing.ImportElement;
import dev.zeddevstuff.mead.core.elements.parsing.StyleElement;
import dev.zeddevstuff.mead.parsing.MeadParser;
import dev.zeddevstuff.mead.parsing.MeadStyleSheetsParser;
import dev.zeddevstuff.mead.styling.*;
import dev.zeddevstuff.mead.utils.NullUtils;
import org.slf4j.Logger;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.jar.JarFile;
import java.util.regex.Pattern;

/**
 * The centralized interface to Mead. It is recommended for you to use this instead of directly using Mead classes.
 */
public class MeadContext
{
    private final Logger LOGGER = LogUtils.getLogger();
    private final UUID key = UUID.randomUUID();
    public final Registry<MeadParser.IMeadElementFactory> elementFactories = new Registry<>(key);
    public final Registry<IMeadStylePropertyApplier> stylePropertyAppliers = new Registry<>(key);
    private final String modid;
    private final Class<?> modClass;

    private final HashMap<String, IntermediaryDOM> intermediaryDOMs = new HashMap<>();
    public Optional<IntermediaryDOM> getIntermediaryDOM(String path)
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
    public MeadContext(String modid, Class<?> modClass)
    {
        checkModClass(modClass);
        this.modid = modid;
        this.modClass = modClass;
        registerDefaults();
        loadMeadDocuments();
        loadStyleSheets();
    }

    private final MeadParser defaultMeadParser = createParser();
    public MeadParser createParser()
    {
        return new MeadParser(this);
    }
    public MeadParser createParser(HashMap<String, Binding<?>> variables, HashMap<String, Callable<?>> actions)
    {
        return new MeadParser(this, variables, actions);
    }
    private final MeadStyleSheetsParser defaultStyleSheetsParser = createStyleSheetsParser();
    public MeadStyleSheetsParser createStyleSheetsParser()
    {
        return new MeadStyleSheetsParser(this);
    }

    private void registerDefaults()
    {
        registerDefaultFactories();
        registerDefaultStylePropertyAppliers();
        registerMeadStyle();
    }
    private void registerDefaultFactories()
    {
        elementFactories.register("Mead", Element::new);
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
                                        defaultMeadParser.parseIntermediary(content)
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
            LOGGER.error("Failed to load mead documents for mod {}", modid, e);
        }
        var size = intermediaryDOMs.size();
        if(size == 1)
            LOGGER.info("Loaded 1 mead document for mod {}", modid);
        else
            LOGGER.info("Loaded {} mead documents for mod {}", intermediaryDOMs.size(), modid);
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
                                            var result = defaultStyleSheetsParser.parse(content);
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
            LOGGER.error("Failed to load style sheets for mod {}", modid, e);
        }
        var size = styleSheets.size();
        if(size == 1)
            LOGGER.info("Loaded 1 stylesheet for mod {}", modid);
        else
            LOGGER.info("Loaded {} stylesheets for mod {}", styleSheets.size(), modid);
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
