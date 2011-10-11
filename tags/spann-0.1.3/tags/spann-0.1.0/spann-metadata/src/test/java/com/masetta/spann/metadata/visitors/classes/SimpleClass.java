/**
 * Copyright 2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.masetta.spann.metadata.visitors.classes;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class SimpleClass extends PreventAccess implements VisitedInterface {
    
    private static boolean staticField;
    
    private static final boolean staticFinalField = true;
    
    private Date privateField;
    
    private final Date privateFinalField = new Date();
    
    protected String protectedField;
    
    protected final String protectedFinalField = "";
    
    int defaultField;
    
    final int defaultFinalField = 3;
    
    public List publicField;
    
    public final List publicFinalField = new ArrayList();
    
    private volatile Integer volatileField;
    
    private transient Float transientField;
    
    private int[] i;
    
    private int[][] ii;
    
    private int [][][] iii;
    
    private String[] s1;
    
    private String[][] s2;
    
    private String[][][] s3;
    
    // ---------------------------------------------------------------------------------------------
    
    public String interfaceMethod() {
        return null;
    }
    
    public void publicVoidMethod( String arg ) {
    
    }
    
    protected String protectedMethod( String arg1 , int arg2 ) {
        return null;
    }
    
    boolean defaultMethod( Date date , long l ) {
        return true;
    }
    
    private int privateMethod( int arg1 , float arg2 ) {
        return 0;
    }
    
    public void throwingMethod() throws IOException, ParseException {
    }
    
}
