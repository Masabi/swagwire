package com.masabi.schwitfy;

import io.swagger.codegen.*;
import io.swagger.codegen.languages.AbstractJavaCodegen;
import io.swagger.codegen.languages.JavaClientCodegen;
import io.swagger.models.Model;
import io.swagger.models.Operation;
import io.swagger.models.Swagger;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.net.URL;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public class WireswagCodegen extends JavaClientCodegen {
    private static final String CLASS_PREFIX = "RemotelyMocked";

    public WireswagCodegen() {
        this.supportedLibraries.put("wireswag", "Test client generation");
    }

    @Override
    public String getName() {
        return "wireswag";
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
        setLibrary("wireswag");

        super.processOpts();

        modelTemplateFiles.remove("model.mustache");
        modelTemplateFiles.put("wireswag/model.mustache", ".java");
        apiTemplateFiles.remove("api.mustache");
        apiTemplateFiles.put("wireswag/api.mustache", ".java");

        Set<String> templateFilesToKeep = new HashSet<String>() {{
            add("JSON.mustache");
        }};
//        this.supportingFiles.removeIf(f -> !templateFilesToKeep.contains(f.templateFile));
        this.supportingFiles.clear();
        this.apiTestTemplateFiles.clear();
        String invokerFolder = (this.sourceFolder + '/' + this.invokerPackage).replace(".", "/");

        this.supportingFiles.add(new SupportingFile("wireswag/apiresponse.mustache", invokerFolder, "RemoteOperation.java"));
        this.supportingFiles.add(new SupportingFile("wireswag/wiremock_api_response.mustache", invokerFolder, "WireMockedRemoteOperation.java"));
        this.supportingFiles.add(new SupportingFile("wireswag/apiutils.mustache", invokerFolder, "ApiUtils.java"));
//        this.supportingFiles.add(new SupportingFile("wireswag/enum_typeadapterfactory.mustache", invokerFolder, "EnumTypeAdapterFactory.java"));
//            setUseRuntimeException(true);

        this.additionalProperties.put("gson", "true");
        setJava8Mode(true);
    }
}
