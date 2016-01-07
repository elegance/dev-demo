### 单文件编译流程，其中插件扩展点包括：
1. lint：代码校验检查，比较特殊，所以需要 release 命令命令行添加 -l 参数
2. parser：预处理阶段，比如 less、sass、es6、react 前端模板等都在此处预编译处理
3. preprocessor：标准化前处理插件
4. standard：标准化插件，处理内置语法
5. postprocessor：标准化后处理插件