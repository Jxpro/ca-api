# X509CA API

## 介绍

此为课程设计（CA系统）的API接口，项目采用了前后端分离进行开发，前端用户UI参见[ca-web](https://github.com/Jxpro/ca-web)

> 设计要求：
>
> 1. 展示有效证书列表和撤销列表，提供证书文件和CRL的下载
> 2. 接受用户的提交申请，包括用户信息和公钥，公钥由用户自己产生
> 3. 用户申请认证的过程，（可选）储存相应的电子文档，如营业执照PDF
> 4. 管理员审核认证信息，给通过的用户颁发证书，拒绝的用户则需重新申请
> 5. 用户密钥丢失或其他情况时，可以吊销证书，密钥作废，并更新撤销列表和CRL

## 组件说明

其中用到的主要开源技术及组件有：

1. [Spring框架](https://spring.io/)：Java Web必备框架
2. [Mybatis-Plus](https://baomidou.com/)： [MyBatis](https://github.com/mybatis)的增强工具，持久层框架
3. [bouncycastle](https://www.bouncycastle.org/)：轻量密码包，包含大量密码算法，且支持ECC
4. [jjwt](https://github.com/jwtk/jjwt)：一种 `JWT` 的实现方案，其他实现方案介绍和对比参考[此文章](https://www.cnblogs.com/hlkawa/p/13675792.html)
5. [fastjson2](https://github.com/alibaba/fastjson2)：用于将 `Java` 对象转换为其 `JSON` 表示形式，方便直接写入响应体
6. [x509-utils](https://github.com/Jxpro/x509-utils)：自己封装的证书工具类，用于生成证书和 `CRL` ，仓库中有不同实现方法的介绍

## 联系方式

如果有任何问题，欢迎到 [github issue](https://github.com/Jxpro/damai-tickets/issues) 进行讨论，或发送电子邮件到 [jxpro@qq.com](mailto:jxpro@qq.com) 来联系我
