package top.phakeandy.youchat.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
    info =
        @Info(
            title = "YouChat RESTful API",
            version = "1.0",
            description = "YouChat 是一个现代化的即时通讯应用 API，提供用户管理、好友关系、群聊和文件传输等功能。",
            contact = @Contact(name = "YouChat Team", email = "contact@youchat.com")),
    servers = {
      @Server(url = "http://localhost:8080", description = "开发服务器"),
      @Server(url = "https://youchat.phakeandy.top", description = "生产环境")
    },
    security = @SecurityRequirement(name = "SessionAuth"))
@SecurityScheme(
    name = "SessionAuth",
    type = SecuritySchemeType.APIKEY,
    in = SecuritySchemeIn.COOKIE,
    description = "基于 Session 的身份验证。用户登录成功后，服务器会创建 HTTP Session 并通过 Cookie 返回 session ID。",
    paramName = "SESSION")
public class OpenapiConfig {}
