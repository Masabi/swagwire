package com.github.tddmonkey.swerver;

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

public class SwerverCodegen extends JavaClientCodegen {
    public SwerverCodegen() {
        this.supportedLibraries.put("swerver", "Test client generation");
    }

    @Override
    public String getName() {
        return "swerver";
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
        return "RemotelyMocked" + super.toApiName(name);
    }

    @Override
    public String toModelName(String name) {
        return "RemotelyMocked" + super.toModelName(name);
    }

    @Override
    public void processOpts() {
        setLibrary("swerver");

        super.processOpts();

        modelTemplateFiles.remove("model.mustache");
        modelTemplateFiles.put("swerver/model.mustache", ".java");
        apiTemplateFiles.remove("api.mustache");
        apiTemplateFiles.put("swerver/api.mustache", ".java");

        Set<String> templateFilesToKeep = new HashSet<String>() {{
            add("JSON.mustache");
        }};
//        this.supportingFiles.removeIf(f -> !templateFilesToKeep.contains(f.templateFile));
        this.supportingFiles.clear();
        this.apiTestTemplateFiles.clear();
        String invokerFolder = (this.sourceFolder + '/' + this.invokerPackage).replace(".", "/");

        this.supportingFiles.add(new SupportingFile("swerver/apiresponse.mustache", invokerFolder, "RemoteOperation.java"));
        this.supportingFiles.add(new SupportingFile("swerver/wiremock_api_response.mustache", invokerFolder, "WireMockedRemoteOperation.java"));
        this.supportingFiles.add(new SupportingFile("swerver/apiutils.mustache", invokerFolder, "ApiUtils.java"));
//        this.supportingFiles.add(new SupportingFile("swerver/enum_typeadapterfactory.mustache", invokerFolder, "EnumTypeAdapterFactory.java"));
//            setUseRuntimeException(true);

        this.additionalProperties.put("gson", "true");
        setJava8Mode(true);
    }
}
