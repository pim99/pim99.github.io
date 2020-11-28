package br.unip.ads.pim.config.docs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.tags.Tag;

/**
 * https://stackoverflow.com/a/49029740/3072570
 */
@Configuration
public class OpenApiConfig {

	public static final String TAG_LOGIN = "Login";
	public static final String TAG_INTERESSE = "Interesse";
	public static final String TAG_USUARIOS = "Usuarios";
	public static final String TAG_RELATORIOS = "Relatorios";
	
    private static final String BASIC_AUTH_SCHEME_NAME = "basicAuth";

    @Bean
    public OpenAPI customOpenAPI() {
        Info info = new Info()
                .title("API do PIM")
                .description("API RESTful do PIM da turma de ADS.")
                .version("1.0.0");

		SecurityScheme basicAuthScheme = new SecurityScheme()
		    .name(BASIC_AUTH_SCHEME_NAME)
		    .type(SecurityScheme.Type.HTTP)
		    .scheme("basic");
		
		return new OpenAPI().components(new Components())
                .addTagsItem(createTag(TAG_LOGIN, "Operações no domínio de Login."))
                .addTagsItem(createTag(TAG_INTERESSE, "Operações no domínio de Interesse."))
                .addTagsItem(createTag(TAG_USUARIOS, "Operações no domínio de Usuarios."))
                .addTagsItem(createTag(TAG_RELATORIOS, "Operações no domínio de Relatorios."))
                .addSecurityItem(new SecurityRequirement().addList(BASIC_AUTH_SCHEME_NAME))
                .components(new Components().addSecuritySchemes(BASIC_AUTH_SCHEME_NAME, basicAuthScheme))
                .info(info);
    }

    private Tag createTag(String name, String description) {
        Tag tag = new Tag();
        tag.setName(name);
        tag.setDescription(description);
        return tag;
    }

}