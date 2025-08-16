package dev.zeddevstuff.mead.core.parsing;

import dev.zeddevstuff.mead.core.ElementState;
import dev.zeddevstuff.mead.core.MeadContext;
import dev.zeddevstuff.mead.core.styling.MeadStyle;
import dev.zeddevstuff.mead.core.styling.MeadStyleRule;
import net.minecraft.util.Tuple;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

public class MeadStyleSheetsParser
{
    public final static Pattern COMMENT_PATTERN = Pattern.compile("/\\*[^*]*\\*+(?:[^/*][^*]*\\*+)*/", Pattern.DOTALL);
    public final static Pattern NEWLINE_PATTERN = Pattern.compile("\\r?\\n");
    public final static Pattern RULE_PATTERN = Pattern.compile("(?<rule>\\.?[^ \\r\\s]+)\\s*\\{(?<content>(?:[\\r\\n]|[^}]*)*)}", Pattern.DOTALL);

    public static Optional<MeadStyle> parse(MeadContext ctx, String styleSheet)
    {
        var cleanedStyleSheet = removeComments(styleSheet);
        cleanedStyleSheet = flatten(cleanedStyleSheet);
        var rules = extractRules(cleanedStyleSheet);
        MeadStyle style = new MeadStyle(ctx);
        for(var rule : rules)
        {
            String ruleName = rule.getA();
            String ruleContent = rule.getB();
            String[] properties = extractProperties(ruleContent);
            var targetType = ruleName.startsWith(".") ? MeadStyleRule.TargetType.STYLE : MeadStyleRule.TargetType.TAG;
            var targetName = ruleName.startsWith(".") ? ruleName.substring(1) : ruleName;
            var targetState = ElementState.NORMAL;
            if(ruleName.contains(":"))
            {
                String[] parts = ruleName.split(":");
                if(parts.length != 2)
                    throw new IllegalArgumentException("Invalid rule name format: " + ruleName);
                targetName = parts[0].trim();
                targetState = switch (parts[1].trim().toUpperCase()) {
                    case "HOVER" -> ElementState.HOVER;
                    case "ACTIVE" -> ElementState.ACTIVE;
                    case "DISABLED" -> ElementState.DISABLED;
                    default -> ElementState.NORMAL;
                };
            }
            MeadStyleRule styleRule = new MeadStyleRule(
                targetType,
                targetName,
                targetState,
                Arrays.stream(properties)
                    .map(prop -> {
                        String[] parts = prop.split(":");
                        return new MeadStyleRule.MeadStyleProperty(parts[0].trim(), parts[1].trim());
                    })
                    .toList()
            );
            style.addRule(styleRule);
        }
        return Optional.of(style);
    }
    private static String removeComments(String input)
    {
        if (input == null || input.isEmpty())
        {
            return input;
        }
        return COMMENT_PATTERN.matcher(input).replaceAll("");
    }
    // Remove all newlines but preserve whitespace
    private static String flatten(String input)
    {
        if (input == null || input.isEmpty())
        {
            return input;
        }
        return NEWLINE_PATTERN.matcher(input).replaceAll("");
    }
    private static List<Tuple<String, String>> extractRules(String styleSheet)
    {
        if (styleSheet == null || styleSheet.isEmpty())
        {
            return new ArrayList<>();
        }
        return RULE_PATTERN.matcher(styleSheet).results()
            .map(matchResult -> new Tuple<>(
                matchResult.group("rule").trim(),
                matchResult.group("content").trim()
                ))
            .toList();
    }
    private static String[] extractProperties(String ruleContent)
    {
        if (ruleContent == null || ruleContent.isEmpty())
        {
            return new String[0];
        }
        // Split by semicolon and trim each property
        return Arrays.stream(ruleContent.split(";"))
            .map(String::trim)
            .filter(prop -> !prop.isEmpty())
            .toArray(String[]::new);
    }
}
