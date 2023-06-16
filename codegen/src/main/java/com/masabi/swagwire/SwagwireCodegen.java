package com.masabi.swagwire;

import org.openapitools.codegen.CodegenType;
import org.openapitools.codegen.languages.AbstractJavaCodegen;

public class SwagwireCodegen extends AbstractJavaCodegen {
    private static final String CLASS_PREFIX = "SwagWired";

    public SwagwireCodegen() {
        setupTemplateFiles();
    }

    private void setupTemplateFiles() {
        embeddedTemplateDir = templateDir = "swagwire";
        modelTemplateFiles.clear();
        apiTemplateFiles.clear();
        apiTestTemplateFiles.clear();
        modelTestTemplateFiles.clear();
        supportingFiles.clear();
        apiDocTemplateFiles.clear();
        modelDocTemplateFiles.clear();

        modelTemplateFiles.put("model.mustache", ".java");
        apiTemplateFiles.put("api.mustache", ".java");
    }


    @Override
    public String getName() {
        return "swagwire";
    }

    @Override
    public CodegenType getTag() {
        return CodegenType.OTHER;
    }

    @Override
    public String toApiName(String name) {
        return CLASS_PREFIX + super.toApiName(name);
    }

    @Override
    public String toModelName(String name) {
        return CLASS_PREFIX + super.toModelName(name);
    }

    @Override
    public void processOpts() {
        super.processOpts();

        this.additionalProperties.put("gson", "true");
        setDateLibrary("java8");
    }
}
