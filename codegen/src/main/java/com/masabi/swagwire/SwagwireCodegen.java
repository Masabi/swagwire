package com.masabi.swagwire;

import io.swagger.codegen.*;
import io.swagger.codegen.languages.JavaClientCodegen;
import io.swagger.codegen.languages.KotlinClientCodegen;

import java.util.HashSet;
import java.util.Set;

public class SwagwireCodegen extends KotlinClientCodegen {
    private static final String CLASS_PREFIX = "SwagWired";

    public SwagwireCodegen() {
        this.supportedLibraries.put("swagwire", "Test client generation");
        overrideTypeMappings();
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
        if (needsMapping(name)) {
            return CLASS_PREFIX + super.toModelName(name);
        } else {
            return super.toModelName(name);
        }
    }

    private boolean needsMapping(String name) {
        return !name.contains(".");
    }

    @Override
    public void processOpts() {
        setLibrary("swagwire");

        super.processOpts();

        modelTemplateFiles.remove("model.mustache");
        modelTemplateFiles.put("swagwire/model.mustache", ".kt");
        apiTemplateFiles.remove("api.mustache");
        apiTemplateFiles.put("swagwire/api.mustache", ".java");

        Set<String> templateFilesToKeep = new HashSet<String>() {{
            add("JSON.mustache");
        }};
        this.supportingFiles.removeIf(f -> !templateFilesToKeep.contains(f.templateFile));
        this.supportingFiles.clear();
        this.apiTestTemplateFiles.clear();
//        String invokerFolder = (this.sourceFolder + '/' + this.invokerPackage).replace(".", "/");

//        this.supportingFiles.add(new SupportingFile("swagwire/apiresponse.mustache", invokerFolder, "RemoteOperation.java"));
//        this.supportingFiles.add(new SupportingFile("swagwire/wiremock_api_response.mustache", invokerFolder, "RemoteOperation.java"));
//        this.supportingFiles.add(new SupportingFile("swagwire/apiutils.mustache", invokerFolder, "ApiUtils.java"));
//        this.supportingFiles.add(new SupportingFile("swagwire/enum_typeadapterfactory.mustache", invokerFolder, "EnumTypeAdapterFactory.java"));
//        setUseRuntimeException(true);

        // TODO Make APiUtils inline or just ships as part of code - dunno why we generate it
        this.additionalProperties.put("gson", "true");
//        setJava8Mode(true);
    }

    private void overrideTypeMappings() {
        // ByteArray translation comes from io.swagger.codegen.DefaultCodegen.getSwaggerType()
        // we handle ByteArray the same way BigDecimal is handled in io.swagger.codegen.languages.KotlinClientCodegen
        this.importMapping.put("ByteArray", "kotlin.ByteArray");

        this.typeMapping.put("array", "kotlin.collections.List");
        this.typeMapping.put("list", "kotlin.collections.List");
        this.instantiationTypes.put("array", "listOf");
        this.instantiationTypes.put("list", "listOf");

        // we prefer OffsetDateTime
        String dateTimeType = "java.time.OffsetDateTime";
        this.typeMapping.put("date-time", dateTimeType);
        this.typeMapping.put("date", dateTimeType);
        this.typeMapping.put("Date", dateTimeType);
        this.typeMapping.put("DateTime", dateTimeType);
        this.importMapping.put("DateTime", dateTimeType);
        this.importMapping.put("LocalDateTime", dateTimeType);
    }
}
