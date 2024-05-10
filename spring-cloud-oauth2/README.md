# 统一授权认证中心

- oauth2-uaa-server：认证中心
- oauth2-client-order：使用springSecurity来实现自动单点登录，非前后端分离
- oauth2-web-client-user：前后端分离的单点登录与单点登出



## OAuth2标准接口
- /oauth/authorize：授权端点
- /oauth/token：获取令牌端点
- /oauth/confirn_access：哟普农户确认授权提交端点
- /oauth/error：授权服务器错误信息端点
- /oauth/check_token：用于资源服务访问的令牌解析端点
- /oauth/token_key：提供共有密钥的端点，如果使用JWT令牌的话

