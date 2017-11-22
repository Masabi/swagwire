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
import java.util.Map;
import java.util.regex.Pattern;

public class SwerverCodegen extends JavaClientCodegen {
    public SwerverCodegen() {
        this.supportedLibraries.put("swerver", "Test client generation");
//        apiTemplateFiles.put("api.mustache", ".java");
//        this.cliOptions.forEach(opt -> System.out.println("opt is " + opt.getOpt()));
//        System.out.println(this.supportedLibraries);
//        this.cliOptions.removeIf(option -> option.getOpt().equals("library"));
//        CliOption libraryOption = new CliOption("library", "library template (sub-template) to use");
//        libraryOption.setDefault("okhttp-gson");
//        this.cliOptions.add(libraryOption);
//        libraryOption.setEnum(this.supportedLibraries);
//        setLibrary("swerver");
    }

    @Override
    public String getName() {
        return "swerver";
    }

    @Override
    public String getLibrary() {
        return "swerver";
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
        super.processOpts();
//        super.processOpts();
//        if ("swerver".equals(this.getLibrary())) {
            System.out.println("USING SWERVER CONFIG for " + getInputSpec());
//            this.templateDir = "templates/";
            this.supportingFiles.clear();
//            setLibrary("swerver");
            this.apiTestTemplateFiles.clear();
        String invokerFolder = (this.sourceFolder + '/' + this.invokerPackage).replace(".", "/");

        this.supportingFiles.add(new SupportingFile("apiresponse.mustache", invokerFolder, "RemoteOperation.java"));
        this.supportingFiles.add(new SupportingFile("wiremock_api_response.mustache", invokerFolder, "WireMockedRemoteOperation.java"));
        this.supportingFiles.add(new SupportingFile("apiutils.mustache", invokerFolder, "ApiUtils.java"));
            this.apiTemplateFiles.entrySet().forEach(e -> {
                System.out.println("Api template file " + e.getKey() + " -  " + e.getValue());
            });
            String tag = "Pet";
            String filename = apiFilename("api.mustache", tag);
                System.out.println("filename " + filename);
//            setUseRuntimeException(true);
            System.out.println("Location: " + getFullTemplateFile(this, "api.mustache"));
            setJava8Mode(true);
//            setLibrary("swerver");
//        } else {
//            super.processOpts();
//        }

//        this.supportingFiles.clear();
//        this.supportedLibraries.put("swerver", "Test client generation");
//        apiTemplateFiles.put("api.mustache", ".java");
//        setLibrary("swerver");
//        URL resource = getClass().getResource("templates/swerver");
//        System.out.println("Reousrce is " + resource);
//        String file = resource.getFile();
//        setTemplateDir("/Users/colin/src/personal/swerver/templates/");
//        System.out.println("Templates is " + file);
    }

    @Override
    public CodegenOperation fromOperation(String path, String httpMethod, Operation operation, Map<String, Model> definitions, Swagger swagger) {
        System.out.println("Operation is " + path);
        System.out.println("Operation is " + operation.getTags());
        return super.fromOperation(path, httpMethod, operation, definitions, swagger);
    }

    /** dont think we need any of this down here **/

    public String getFullTemplateFile(CodegenConfig config, String templateFile) {
        //1st the code will check if there's a <template folder>/libraries/<library> folder containing the file
        //2nd it will check for the file in the specified <template folder> folder
        //3rd it will check if there's an <embedded template>/libraries/<library> folder containing the file
        //4th and last it will assume the file is in <embedded template> folder.

        //check the supplied template library folder for the file
        final String library = config.getLibrary();
        if (StringUtils.isNotEmpty(library)) {
            //look for the file in the library subfolder of the supplied template
            final String libTemplateFile = buildLibraryFilePath(config.templateDir(), library, templateFile);
            if (new File(libTemplateFile).exists()) {
                return libTemplateFile;
            }
        }

        //check the supplied template main folder for the file
        final String template = config.templateDir() + File.separator + templateFile;
        if (new File(template).exists()) {
            return template;
        }

        //try the embedded template library folder next
        if (StringUtils.isNotEmpty(library)) {
            final String embeddedLibTemplateFile = buildLibraryFilePath(config.embeddedTemplateDir(), library, templateFile);
            if (embeddedTemplateExists(embeddedLibTemplateFile)) {
                // Fall back to the template file embedded/packaged in the JAR file library folder...
                return embeddedLibTemplateFile;
            }
        }

        // Fall back to the template file embedded/packaged in the JAR file...
        return config.embeddedTemplateDir() + File.separator + templateFile;
    }

    private String buildLibraryFilePath(String dir, String library, String file) {
        return dir + File.separator + "libraries" + File.separator + library + File.separator + file;
    }

    public boolean embeddedTemplateExists(String name) {
        return this.getClass().getClassLoader().getResource(getCPResourcePath(name)) != null;
    }

    public String getCPResourcePath(String name) {
        if (!"/".equals(File.separator)) {
            return name.replaceAll(Pattern.quote(File.separator), "/");
        }
        return name;
    }



}
