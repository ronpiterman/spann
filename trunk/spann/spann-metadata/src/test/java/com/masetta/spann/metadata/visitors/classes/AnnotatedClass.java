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

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Ann
public class AnnotatedClass {

    @Ann(i = 5)
    @Ann2("i")
    private int i;

    @Ann(ins = { 1, 5 })
    @Ann2("ins")
    private int ins;

    @Ann(fs = { 1f, 5f })
    @Ann2("fs")
    private int fs;

    @Ann(bs = { true, false })
    @Ann2("bs")
    private int bs;

    @Ann(s = "1")
    @Ann2("s")
    private int s;

    @Ann(sts = { "1", "5" })
    @Ann2("sts")
    private int sts;

    @Ann(cls = String.class)
    @Ann2("cls")
    private int cls;

    @Ann(clses = { String.class, Integer.class })
    @Ann2("clses")
    private int clses;

    @Ann(cls1 = Object.class)
    @Ann2("cls1")
    private int cls1;

    @Ann(cls1es = { Object.class, Integer.class })
    @Ann2("cls1es")
    private int cls1es;

    @Ann(cls2 = Float.class)
    @Ann2("cls2")
    private int cls2;

    @Ann(cls2es = { Float.class, String.class, Integer.class })
    @Ann2("cls2es")
    private int cls2es;

    @Ann(en = RetentionPolicy.CLASS)
    @Ann2("en")
    private int en;

    @Ann(ens = RetentionPolicy.CLASS)
    @Ann2("ens")
    private int ens;

    @Ann
    @Ann2("empty")
    private int empty;

    @Ann(i = 5, ins = { 1, 5 }, fs = { 1f, 5f }, bs = { true, false }, s = "1", sts = { "1", "5" }, cls = String.class, clses = {
            String.class, Integer.class }, cls1 = Object.class, cls1es = { Object.class,
            Integer.class }, cls2 = Float.class, cls2es = { Float.class, String.class,
            Integer.class }, en = RetentionPolicy.CLASS, ens = RetentionPolicy.CLASS)
    @Ann2("many")
    private int many;

    @Ann(ann = @AnnAttr(retention = @Retention(RetentionPolicy.CLASS), retentions = {
            @Retention(RetentionPolicy.CLASS), @Retention(RetentionPolicy.SOURCE) }))
    @Ann2("ann")
    private int ann;

    // same as fields, but methods.

    @Ann(i = 5)
    @Ann2("i")
    void i(@Ann(i = 6) @Ann2("i") int i) {
    }

    @Ann(ins = { 1, 5 })
    @Ann2("ins")
    void ins(@Ann(ins = { 2, 6 }) @Ann2("ins") int i) {
    }

    @Ann(fs = { 1f, 5f })
    @Ann2("fs")
    void fs(@Ann(fs = { 2f, 6f }) @Ann2("fs") int i) {
    }

    @Ann(bs = { true, false })
    @Ann2("bs")
    void bs(@Ann(bs = { false, true}) @Ann2("bs") int i) {
    }

    @Ann(s = "1")
    @Ann2("s")
    void s(@Ann(s = "2") @Ann2("s") int i) {
    }

    @Ann(sts = { "1", "5" })
    @Ann2("sts")
    void sts(@Ann(sts = { "2", "6" }) @Ann2("sts") int i) {
    }

    @Ann(cls = String.class)
    @Ann2("cls")
    void cls(@Ann(cls = Object.class) @Ann2("cls") int i) {
    }

    @Ann(clses = { String.class, Integer.class })
    @Ann2("clses")
    void clses(@Ann(clses = { Integer.class, String.class }) @Ann2("clses") int i) {
    }

    @Ann(cls1 = Object.class)
    @Ann2("cls1")
    void cls1(@Ann(cls1 = String.class) @Ann2("cls1") int i) {
    }

    @Ann(cls1es = { Object.class, Integer.class })
    @Ann2("cls1es")
    void cls1es(@Ann(cls1es = { Integer.class, Object.class }) @Ann2("cls1es") int i) {
    }

    @Ann(cls2 = Float.class)
    @Ann2("cls2")
    void cls2(@Ann(cls2 = Integer.class) @Ann2("cls2") int i) {
    }

    @Ann(cls2es = { Float.class, String.class, Integer.class })
    @Ann2("cls2es")
    void cls2es(@Ann(cls2es = { Integer.class, String.class, Float.class }) @Ann2("cls2es") int i) {
    }

    @Ann(en = RetentionPolicy.CLASS)
    @Ann2("en")
    void en(@Ann(en = RetentionPolicy.RUNTIME) @Ann2("en") int i) {
    }

    @Ann(ens = RetentionPolicy.CLASS)
    @Ann2("ens")
    void ens(@Ann(ens = RetentionPolicy.RUNTIME) @Ann2("ens") int i) {
    }

    @Ann
    @Ann2("empty")
    void empty(@Ann @Ann2("empty") int i) {
    }

    @Ann(i = 5, ins = { 1, 5 }, fs = { 1f, 5f }, bs = { true, false }, s = "1", sts = { "1", "5" }, cls = String.class, clses = {
            String.class, Integer.class }, cls1 = Object.class, cls1es = { Object.class,
            Integer.class }, cls2 = Float.class, cls2es = { Float.class, String.class,
            Integer.class }, en = RetentionPolicy.CLASS, ens = RetentionPolicy.CLASS)
            
    @Ann2("many")
    void many(
            @Ann(i = 6, ins = { 2, 6 }, fs = { 2f, 6f }, bs = { false, true }, s = "2", sts = {
                    "2", "6" }, cls = Object.class, clses = { Integer.class, String.class }, cls1 = String.class, cls1es = {
                    Integer.class, Object.class }, cls2 = Integer.class, cls2es = { Integer.class,
                    String.class, Float.class }, en = RetentionPolicy.RUNTIME, ens = RetentionPolicy.RUNTIME) @Ann2("many") int i) {
    }

    @Ann(ann = @AnnAttr(retention = @Retention(RetentionPolicy.CLASS), retentions = {
            @Retention(RetentionPolicy.CLASS), @Retention(RetentionPolicy.SOURCE) }))
    void ann(@Ann(ann = @AnnAttr(retention = @Retention(RetentionPolicy.CLASS), retentions = {
            @Retention(RetentionPolicy.CLASS), @Retention(RetentionPolicy.SOURCE) })) int i) {
    }

}
