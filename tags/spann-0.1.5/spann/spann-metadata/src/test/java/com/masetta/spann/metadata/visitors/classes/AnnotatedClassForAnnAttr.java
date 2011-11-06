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

import javax.swing.text.rtf.RTFEditorKit;

@AnnAttr(retention = @Retention(RetentionPolicy.RUNTIME), retentions = {
		@Retention(RetentionPolicy.SOURCE), @Retention(value = RetentionPolicy.CLASS) })
		
@AnnAttr2(
	attr = @AnnAttr(retention = @Retention(RetentionPolicy.RUNTIME), retentions = {
		@Retention(RetentionPolicy.CLASS), @Retention(value = RetentionPolicy.SOURCE) }), 
		// !
	attrs = {
		@AnnAttr(retention = @Retention(RetentionPolicy.SOURCE), retentions = {
				@Retention(RetentionPolicy.SOURCE) }),
		@AnnAttr(retention = @Retention(RetentionPolicy.RUNTIME), retentions = {
				@Retention(RetentionPolicy.SOURCE), @Retention(value = RetentionPolicy.CLASS) }),
		@AnnAttr(retention = @Retention(RetentionPolicy.CLASS), retentions = {
			@Retention(RetentionPolicy.SOURCE), @Retention(value = RetentionPolicy.CLASS) , @Retention(value = RetentionPolicy.RUNTIME) }) 
	})
	
@AnnAttr3(
	attr = @AnnAttr2(
			attr = @AnnAttr(retention = @Retention(RetentionPolicy.RUNTIME), retentions = {
				@Retention(RetentionPolicy.SOURCE), @Retention(value = RetentionPolicy.CLASS) }), 
				
			attrs = {
				@AnnAttr(retention = @Retention(RetentionPolicy.RUNTIME), retentions = {
						@Retention(RetentionPolicy.SOURCE), @Retention(value = RetentionPolicy.CLASS) }),
				@AnnAttr(retention = @Retention(RetentionPolicy.RUNTIME), retentions = {
						@Retention(RetentionPolicy.SOURCE), @Retention(value = RetentionPolicy.CLASS) }) }),
	attrs={}
)
public class AnnotatedClassForAnnAttr {

}
