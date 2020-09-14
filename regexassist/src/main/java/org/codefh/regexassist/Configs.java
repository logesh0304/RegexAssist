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

package org.codefh.regexassist;

import java.util.Map;
import java.util.TreeMap;

/**
 * This class has the configuration required by App
 */
public class Configs
{   
    public static final Map<String,Integer> flagsNvalue=new TreeMap<>();
    public static boolean renderInvisibleChars=true;
    public static int rgxAreaMaxRow=4, replaceTextMaxRow=4;
    public static boolean autoMatch=true,
            autoSplit=false,
            autoReplace=false;
    public static int flagInt=0;

    static {
        
        flagsNvalue.put("CANNON_EQ", 128);
        flagsNvalue.put("CASE_INSENSITIVE",2);
        flagsNvalue.put("COMMENTS",4);
        flagsNvalue.put("DOTALL",32);
        flagsNvalue.put("LITERAL",16);
        flagsNvalue.put("MULTILINE",8);
        flagsNvalue.put("UNICODE_CASE",64);
        flagsNvalue.put("UNICODE_CHRACTER_CLASS",256);
        flagsNvalue.put("UNICODE_LINES",1);
    }
    
    
}
