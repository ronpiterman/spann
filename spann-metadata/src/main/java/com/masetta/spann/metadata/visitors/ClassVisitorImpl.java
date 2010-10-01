
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
 *
 * @author rpt
 * @version $Id: $
 */

package com.masetta.spann.metadata.visitors;

import com.masetta.spann.metadata.common.Artifact;
import com.masetta.spann.metadata.common.ArtifactElement;
import com.masetta.spann.metadata.common.ResourceUtil;
import com.masetta.spann.metadata.core.ClassMetadata;
import com.masetta.spann.metadata.core.FieldMetadata;
import com.masetta.spann.metadata.core.MethodMetadata;
import com.masetta.spann.metadata.core.modifier.ClassModifier;
import com.masetta.spann.metadata.core.modifier.FieldModifier;
import com.masetta.spann.metadata.core.modifier.MethodModifier;
import com.masetta.spann.metadata.core.support.FieldMetadataSupport;
import com.masetta.spann.metadata.reader.VisitorAdapter;
public class ClassVisitorImpl extends AbstractVisitor<ClassMetadataImpl> {
	
	private VisitorAdapter<FieldVisitorImpl> emptyFieldVisitor;
	
	private VisitorAdapter<MethodVisitorImpl> emptyMethodVisitor;
	
    /**
     * <p>Constructor for ClassVisitorImpl.</p>
     *
     * @param controller a {@link com.masetta.spann.metadata.visitors.VisitorController} object.
     */
    @SuppressWarnings("unchecked")
	public ClassVisitorImpl(VisitorController controller) {
        super(ClassMetadataImpl.class, controller);
        this.emptyFieldVisitor = (VisitorAdapter<FieldVisitorImpl>) 
        	controller.getEmptyVisitorAdapter( Artifact.FIELD );
        this.emptyMethodVisitor = (VisitorAdapter<MethodVisitorImpl>) 
        	controller.getEmptyVisitorAdapter( Artifact.METHOD );
    }

    /**
     * <p>visit</p>
     *
     * @param version a int.
     * @param access a int.
     * @param name a {@link java.lang.String} object.
     * @param signature a {@link java.lang.String} object.
     * @param superName a {@link java.lang.String} object.
     * @param interfaces an array of {@link java.lang.String} objects.
     */
    public void visit(int version, int access, String name, String signature,
            String superName, String[] interfaces) {
    	ClassMetadata clsMetadata = getClassMetadata( name, 0 );
    	visit( ClassVisitorImpl.class, clsMetadata, ArtifactElement.SELF );
        ClassModifier modifier = getVisitorDelegate().createClassModifier( access );
        getMetadata().init(modifier, signature, getClassMetadata(superName,0),
                getClassMetadata(interfaces));
    }

    /**
     * <p>visitField</p>
     *
     * @param access a int.
     * @param name a {@link java.lang.String} object.
     * @param desc a {@link java.lang.String} object.
     * @param signature a {@link java.lang.String} object.
     * @param value a {@link java.lang.Object} object.
     * @return a {@link com.masetta.spann.metadata.reader.VisitorAdapter} object.
     */
    public VisitorAdapter<FieldVisitorImpl> visitField(int access, String name, String desc,
            String signature, Object value) {
        if (!isVisit(ArtifactElement.FIELDS))
            return emptyFieldVisitor;
        FieldMetadata field = null;
        if ( getMetadata().isLoaded( ArtifactElement.FIELDS ) ) {
            field = FieldMetadataSupport.findField( getMetadata(), name ); 
        }
        if ( field == null) {
            ClassMetadata type = getVisitorDelegate().resolveDescriptor( desc, false );
            FieldModifier modifier = getVisitorDelegate().createFieldModifier( access);
            field = new FieldMetadataImpl(getMetadata(), modifier,
                    name, type , signature );
            getMetadata().addField(field);
        }
        return visit(FieldVisitorImpl.class, field, ArtifactElement.FIELDS);
    }

    /**
     * <p>visitInnerClass</p>
     *
     * @param name a {@link java.lang.String} object.
     * @param outerName a {@link java.lang.String} object.
     * @param innerName a {@link java.lang.String} object.
     * @param access a int.
     */
    public void visitInnerClass(String name, String outerName,
            String innerName, int access) {
        ClassMetadata cls = getClassMetadata(ResourceUtil.convertResourcePathToClassName(name),0);
        getMetadata().addInnerClass(cls);
        if (cls.getOuterClass() != null) {
            return;
        }
        ClassMetadata outer = getClassMetadata(ResourceUtil
                .convertResourcePathToClassName(outerName),0);
        ClassModifier modifier = getVisitorDelegate().createClassModifier(
                access );
        ((ClassMetadataImpl) cls).setInnerOf(modifier, outer);
    }

    /**
     * <p>visitMethod</p>
     *
     * @param access a int.
     * @param name a {@link java.lang.String} object.
     * @param desc a {@link java.lang.String} object.
     * @param signature a {@link java.lang.String} object.
     * @param exceptions an array of {@link java.lang.String} objects.
     * @return a {@link com.masetta.spann.metadata.reader.VisitorAdapter} object.
     */
    public VisitorAdapter<MethodVisitorImpl> visitMethod(int access, String name, String desc,
            String signature, String[] exceptions) {
        if (!isVisit(ArtifactElement.METHODS))
            return emptyMethodVisitor;
        MethodMetadata mm = null;
        String key = name + "::" + desc;
        if ( getMetadata().isLoaded( ArtifactElement.METHODS ) ) {
            mm = getMetadata().findMethod( key );
        }
        if ( mm == null ) {
            MethodModifier modifier = getVisitorDelegate().createMethodModifier( access );
            ClassMetadata returnType = getVisitorDelegate().getMethodReturnType( desc );
            ClassMetadata[] arguments = getVisitorDelegate().getMethodArguments( desc );
            ClassMetadata[] exs = getClassMetadata( exceptions );
            mm = new MethodMetadataImpl( getMetadata() , modifier , name , returnType,
                arguments , exs , signature );
            getMetadata().addMethod( key, mm );
        }
        return visit( MethodVisitorImpl.class , mm, ArtifactElement.METHODS );
    }

    /**
     * <p>visitOuterClass</p>
     *
     * @param owner a {@link java.lang.String} object.
     * @param name a {@link java.lang.String} object.
     * @param desc a {@link java.lang.String} object.
     */
    public void visitOuterClass(String owner, String name, String desc) {
        if ( getMetadata().getOuterClass() == null )
            getMetadata().setInnerOf( null , getClassMetadata( 
                    ResourceUtil.convertResourcePathToClassName( owner ) , 0 ) );
    }

}
