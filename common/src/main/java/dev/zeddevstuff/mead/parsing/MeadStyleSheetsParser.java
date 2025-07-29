package dev.zeddevstuff.mead.parsing;

import dev.zeddevstuff.mead.core.MeadContext;
import dev.zeddevstuff.mead.styling.MeadStyle;
import dev.zeddevstuff.mead.styling.MeadStyleRule;
import net.minecraft.util.Tuple;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

public class MeadStyleSheetsParser
{
    private final static Pattern COMMENT_PATTERN = Pattern.compile("/\\*[^*]*\\*+(?:[^/*][^*]*\\*+)*/", Pattern.DOTALL);
    private final static Pattern NEWLINE_PATTERN = Pattern.compile("\\r?\\n");
    private final static Pattern RULE_PATTERN = Pattern.compile("(?<rule>\\.?[^ \\r\\s]+)\\s*\\{(?<content>(?:[\\r\\n]|[^}]*)*)}", Pattern.DOTALL);

    private final MeadContext ctx;
    public MeadStyleSheetsParser(MeadContext ctx)
    {
        this.ctx = ctx;
    }
    public Optional<MeadStyle> parse(String styleSheet)
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
            MeadStyleRule styleRule = new MeadStyleRule(
                ruleName.startsWith(".") ? MeadStyleRule.TargetType.STYLE : MeadStyleRule.TargetType.TAG,
                ruleName.startsWith(".") ? ruleName.substring(1) : ruleName,
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
    private String removeComments(String input)
    {
        if (input == null || input.isEmpty())
        {
            return input;
        }
        return COMMENT_PATTERN.matcher(input).replaceAll("");
    }
    // Remove all newlines but preserve whitespace
    private String flatten(String input)
    {
        if (input == null || input.isEmpty())
        {
            return input;
        }
        return NEWLINE_PATTERN.matcher(input).replaceAll("");
    }
    private List<Tuple<String, String>> extractRules(String styleSheet)
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
    private String[] extractProperties(String ruleContent)
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
