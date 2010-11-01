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

import java.io.Serializable;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE,ElementType.METHOD,ElementType.FIELD,ElementType.PARAMETER,ElementType.PACKAGE})
@AnnAttr(retention=@Retention(RetentionPolicy.RUNTIME), retentions={})
public @interface Ann {
    
    int i() default 0;
    
    int[] ins() default {1};
    
    float[] fs() default {2f};
    
    boolean[] bs() default false;
    
    String s() default "x";
    
    String[] sts() default {"a","b","c"};
    
    Class cls() default Object.class;
    
    Class[] clses() default {Ann.class};
    
    Class<?> cls1() default String.class;
    
    Class<?>[] cls1es() default { String.class };
    
    Class<? extends Serializable> cls2() default Integer.class;
    
    Class<? extends Serializable>[] cls2es() default { Integer.class , Float.class };
    
    RetentionPolicy en() default RetentionPolicy.RUNTIME;
    
    RetentionPolicy[] ens() default { RetentionPolicy.SOURCE, RetentionPolicy.SOURCE };
    
    AnnAttr ann() default @AnnAttr(retention=@Retention(RetentionPolicy.RUNTIME),retentions={});
    
    AnnAttr[] anns() default { @AnnAttr(retention=@Retention(RetentionPolicy.SOURCE),retentions={}) };

}
