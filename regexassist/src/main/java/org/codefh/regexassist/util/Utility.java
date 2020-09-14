/*
 * The MIT License
 *
 * Copyright 2020 Logesh0304.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package org.codefh.regexassist.util;

import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

/**
 * This class contains utility functions to be used
 */
public class Utility
{
    /**
     * copies the given string to clipboard
     * @param toCopy string to be copied into clipboard 
     * @return true if the given string successfully copied into clipboard, false otherwise
     */
    public static boolean copyToClipboard(String toCopy) {
        ClipboardContent content = new ClipboardContent();
        content.putString(toCopy);
        return Clipboard.getSystemClipboard().setContent(content);
    }

    /**
     * returns a string by replacing invisible(non-printable) characters by suitable representation
     * @param string string to render
     * @return rendered String
     */
    public static String renderInvisibleChars(String string) {
        // 2506 - vertical triple dash light
        // 2507 - verticle triple dash dark
        // 204B - Pilcrow
        // 21B5 - return
        // 21B8 - return with extra height
        // 00B7 - middle dot
        // 27F6 - long right arrow
        // 21A9 - left arrow with hook
        // 2218 - ring operator
        // 2219 - bullet operator
        if (string.isEmpty()) {
            return "\u2507";
        }
        return string.replaceAll("(\r\n|\n|\r)", "\u21b5$1").replace(' ', '\u00b7').replace('\t', '\u27f6');
    }
    
    
}
