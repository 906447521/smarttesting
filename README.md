
是一款基于接口的测试框架，可以进行测试用例脚本编写，批量执行脚本，支持不同项目之间的管理，支持多人协作！


说明
=============
# 结构
    server   编译直接运行
    web      放到tomcat下运行
    services 代码
    
# 框架

    https://materializecss.com/about.html
    jdk8
    tomcat8-embed
    springmvc4
    springframework4
    mybatis3
    mysql5
    
    字体图标地址
    http://127.0.0.1:6060/s/mono/demo_fontclass.html
    彩色图标地址
    http://127.0.0.1:6060/s/colour/demo_symbol.html

# 配置
    编译后home/spring，开发环境src/main/resources/spring目录
    启动类                 smarttesting.Main
    [spring.xml]          : spring load配置
    [spring-view.xml]     : spring velocity配置
    [spring-data.xml]     : 数据库设置，修改jdbcUrl的ip地址与端口

# 启动
    编译后执行 
              
              
    
              smarttesting（linux+macx）              smarttesting.bat（window）
              
              
    调试     ./smarttesting run                       smarttesting.bat run
    启动     ./smarttesting start                     smarttesting.bat start
    停止     ./smarttesting stop                      smarttesting.bat stop
              
              
             installservice（linux+macx）             installservice.bat（window）
              
              
    调试     ./installservice                         installservice.bat install
    删除                                              installservice.bat remove
    
# 测试
    http://gc.ditu.aliyun.com/geocoding?a=成都
    http://qzone-music.qq.com/fcg-bin/cgi_playlist_xml.fcg?uin=QQ号码&json=1&g_tk=1916754934
    https://tcc.taobao.com/cc/json/mobile_tel_segment.htm?tel=13888888888
    
# 插件    
    ./jmeter -n -t baidu_requests_results.jmx -r -l baidu_requests_results.jtl -e -o /home/tester/apache-jmeter-3.0/resultReport
