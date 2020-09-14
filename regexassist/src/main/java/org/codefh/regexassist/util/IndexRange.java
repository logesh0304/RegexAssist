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

/**
 * Class for representing range of index
 */
public class IndexRange
{
    public final static String RANGE_SEPERATOR="-";
    private int start,end;

    /**
     * returns the IndexRange object by given start and end parameters
     * @param start start index of range
     * @param end end index of range
     * @throws IllegalArgumentException when start is less than end
     */
    public IndexRange(int start, int end) {
        if(start>end)
            throw new IllegalArgumentException(String.format("start-index '%d' < end-index '%d", start, end));
        this.start = start;
        this.end = end;
    }
    
    /**
     * return the start index of the range
     * @return start index
     */
    public int getStart() {
        return start;
    }

    /**
     * return the end index of the range
     * @return end index
     */
    public int getEnd() {
        return end;
    }
    
    /**
     * return the length of the range
     * @return length of the range
     */
    public int getlength(){
        return end - start;
    }
    
    /**
     * returns a string representation of index-range (i.e) start and end are separated by '-'
     * @return returns a string representation of index-range
     */
    @Override
    public String toString(){
        return start+" "+RANGE_SEPERATOR+" "+end;
    }
    
    /**
     * returns new instance of IndexRange by parsing the given string representing index-range (i.e) start and end are separated by '-'
     * @param indexRange string representing index-range 
     * @return new instance of IndexRange 
     */
    public static IndexRange from(String indexRange){
        if(indexRange==null)
            throw new IllegalArgumentException("null is not allowed");
        
        String[] range=indexRange.split(RANGE_SEPERATOR);
        if(range.length!=2)
            throw new IllegalArgumentException(indexRange+" is not a valid string representation of range");
        
        return new IndexRange( Integer.parseInt(range[0].trim()), Integer.parseInt(range[2].trim()) );
    }
}