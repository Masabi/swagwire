package com.masabi.swagwire;

import io.swagger.codegen.*;
import io.swagger.codegen.languages.JavaClientCodegen;

import java.util.HashSet;
import java.util.Set;

public class SwagwireCodegen extends JavaClientCodegen {
    private static final String CLASS_PREFIX = "RemotelyMocked";

    public SwagwireCodegen() {
        this.supportedLibraries.put("swagwire", "Test client generation");
    }

    @Override
    public String getName() {
        return "swagwire";
    }

    @Override
    public String getLibrary() {
        return "";
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
        setLibrary("swagwire");

        super.processOpts();

        modelTemplateFiles.remove("model.mustache");
        modelTemplateFiles.put("swagwire/model.mustache", ".java");
        apiTemplateFiles.remove("api.mustache");
        apiTemplateFiles.put("swagwire/api.mustache", ".java");

        Set<String> templateFilesToKeep = new HashSet<String>() {{
            add("JSON.mustache");
        }};
        this.supportingFiles.removeIf(f -> !templateFilesToKeep.contains(f.templateFile));
        this.supportingFiles.clear();
        this.apiTestTemplateFiles.clear();
        String invokerFolder = (this.sourceFolder + '/' + this.invokerPackage).replace(".", "/");

        this.supportingFiles.add(new SupportingFile("swagwire/apiresponse.mustache", invokerFolder, "RemoteOperation.java"));
        this.supportingFiles.add(new SupportingFile("swagwire/wiremock_api_response.mustache", invokerFolder, "WireMockedRemoteOperation.java"));
        this.supportingFiles.add(new SupportingFile("swagwire/apiutils.mustache", invokerFolder, "ApiUtils.java"));
//        this.supportingFiles.add(new SupportingFile("swagwire/enum_typeadapterfactory.mustache", invokerFolder, "EnumTypeAdapterFactory.java"));
        setUseRuntimeException(true);

        this.additionalProperties.put("gson", "true");
        setJava8Mode(true);
    }
}
