/*
 * Copyright © Magento, Inc. All rights reserved.
 * See COPYING.txt for license details.
 */

package com.magento.idea.magento2uct.execution.process;

import com.intellij.execution.process.AnsiEscapeDecoder;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.process.ProcessOutputTypes;
import com.intellij.openapi.util.Key;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jetbrains.annotations.NotNull;

public final class OutputWrapper implements AnsiEscapeDecoder.ColoredTextAcceptor {

    private final ProcessHandler processHandler;
    private final AnsiEscapeDecoder myAnsiEscapeDecoder = new AnsiEscapeDecoder();

    private final Pattern infoWrappedTextPattern = Pattern.compile("(<info>.*<\\/info>)");
    private final Pattern warningWrappedTextPattern = Pattern.compile("(<warning>.*<\\/warning>)");
    private final Pattern errorWrappedTextPattern = Pattern.compile("(<error>.*<\\/error>)");
    private final Pattern criticalWrappedTextPattern =
            Pattern.compile("(<critical>.*<\\/critical>)");

    public OutputWrapper(final @NotNull ProcessHandler processHandler) {
        this.processHandler = processHandler;
    }

    /**
     * Print text with ANSI codes parsed from the supported tags.
     *
     * @param text String
     */
    public void print(final @NotNull String text) {
        String reformattedText = text;
        final Matcher infoGroupsMatcher = infoWrappedTextPattern.matcher(text);
        final Matcher warningGroupsMatcher = warningWrappedTextPattern.matcher(text);
        final Matcher errorGroupsMatcher = errorWrappedTextPattern.matcher(text);
        final Matcher criticalGroupsMatcher = criticalWrappedTextPattern.matcher(text);

        reformattedText = reformatTagsToAnsi(
                reformattedText,
                "<info>",
                "</info>",
                Color.YELLOW,
                infoGroupsMatcher
        );
        reformattedText = reformatTagsToAnsi(
                reformattedText,
                "<warning>",
                "</warning>",
                Color.YELLOW_BOLD,
                warningGroupsMatcher
        );
        reformattedText = reformatTagsToAnsi(
                reformattedText,
                "<error>",
                "</error>",
                Color.RED_BOLD_BACKGROUND,
                errorGroupsMatcher
        );
        reformattedText = reformatTagsToAnsi(
                reformattedText,
                "<critical>",
                "</critical>",
                Color.RED_BRIGHT,
                criticalGroupsMatcher
        );

        myAnsiEscapeDecoder.escapeText(reformattedText, ProcessOutputTypes.STDOUT, this);
    }

    /**
     * Print raw text.
     *
     * @param text String
     */
    public void write(final @NotNull String text) {
        processHandler.notifyTextAvailable(text, ProcessOutputTypes.STDOUT);
    }

    /**
     * Print info text.
     *
     * @param text String
     */
    public void info(final @NotNull String text) {
        myAnsiEscapeDecoder.escapeText(
                Color.YELLOW + text + Color.RESET,
                ProcessOutputTypes.STDOUT,
                this
        );
    }

    /**
     * Print warning text.
     *
     * @param text String
     */
    public void warning(final @NotNull String text) {
        myAnsiEscapeDecoder.escapeText(
                Color.YELLOW_BOLD + text + Color.RESET,
                ProcessOutputTypes.STDOUT,
                this
        );
    }

    /**
     * Print error text.
     *
     * @param text String
     */
    public void error(final @NotNull String text) {
        myAnsiEscapeDecoder.escapeText(
                Color.RED_BOLD_BACKGROUND + text + Color.RESET,
                ProcessOutputTypes.STDOUT,
                this
        );
    }

    /**
     * Print critical text.
     *
     * @param text String
     */
    public void critical(final @NotNull String text) {
        myAnsiEscapeDecoder.escapeText(
                Color.RED_BRIGHT + text + Color.RESET,
                ProcessOutputTypes.STDOUT,
                this
        );
    }

    /**
     * Replace tags with the relevant ANSI codes.
     *
     * @param text String
     * @param openingTag String
     * @param closingTag String
     * @param openingAnsi Color
     * @param matcher Matcher
     *
     * @return String
     */
    private String reformatTagsToAnsi(
            final @NotNull String text,
            final @NotNull String openingTag,
            final @NotNull String closingTag,
            final @NotNull Color openingAnsi,
            final Matcher matcher
    ) {
        String reformattedText = text;

        while (matcher.find()) {
            final String replacingCandidate = matcher.group(0);
            final String reformattedCandidate = replacingCandidate
                    .replace(openingTag, openingAnsi.toString())
                    .replace(closingTag, Color.RESET.toString());
            reformattedText = reformattedText.replace(replacingCandidate, reformattedCandidate);
        }

        return reformattedText;
    }

    @Override
    public void coloredTextAvailable(final @NotNull String text, final @NotNull Key attributes) {
        processHandler.notifyTextAvailable(text, attributes);
    }

    private enum Color {

        RESET("\033[0m"),

        // Regular
        BLACK("\033[0;30m"),
        RED("\033[0;31m"),
        GREEN("\033[0;32m"),
        YELLOW("\033[0;33m"), // INFO
        BLUE("\033[0;34m"),
        MAGENTA("\033[0;35m"),
        CYAN("\033[0;36m"),
        WHITE("\033[0;37m"),

        // Bold
        BLACK_BOLD("\033[1;30m"),
        RED_BOLD("\033[1;31m"),
        GREEN_BOLD("\033[1;32m"),
        YELLOW_BOLD("\033[1;33m"), // WARNING
        BLUE_BOLD("\033[1;34m"),
        MAGENTA_BOLD("\033[1;35m"),
        CYAN_BOLD("\033[1;36m"),
        WHITE_BOLD("\033[1;37m"),

        // Underline
        BLACK_UNDERLINED("\033[4;30m"),
        RED_UNDERLINED("\033[4;31m"),
        GREEN_UNDERLINED("\033[4;32m"),
        YELLOW_UNDERLINED("\033[4;33m"),
        BLUE_UNDERLINED("\033[4;34m"),
        MAGENTA_UNDERLINED("\033[4;35m"),
        CYAN_UNDERLINED("\033[4;36m"),
        WHITE_UNDERLINED("\033[4;37m"),

        // Background
        RED_BOLD_BACKGROUND("\033[48;5;88;1m"), // ERROR

        // High Intensity
        BLACK_BRIGHT("\033[0;90m"),
        RED_BRIGHT("\033[0;91m"), // CRITICAL
        GREEN_BRIGHT("\033[0;92m"),
        YELLOW_BRIGHT("\033[0;93m"),
        BLUE_BRIGHT("\033[0;94m"),
        MAGENTA_BRIGHT("\033[0;95m"),
        CYAN_BRIGHT("\033[0;96m"),
        WHITE_BRIGHT("\033[0;97m");

        private final String code;

        /**
         * Color ENUM constructor.
         *
         * @param code String
         */
        Color(final @NotNull String code) {
            this.code = code;
        }

        @Override
        public String toString() {
            return code;
        }
    }
}
