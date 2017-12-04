# MRouter
导航器，可以通过添加注解@Route，使用导航器来启动Activity

使用方法：

1. 在 Application 的 onCreate() 方法中添加 
```
MRouter.init(this);
```

2. 定义TargetActivity的启动路径 ，在TargetActivity添加@Route注解
```
@Route(ROUTER_SECOND)
public class SecondActivity extends AppCompatActivity {

    public static final String ROUTER_SECOND = "SecondActivity";
    ...
}
```

3. 通过MRouter启动
```
MRouter.build(ROUTER_SECOND).putExtra("extra", "1234extra").start(this); 
```

其他：
1. 可以@Route注解的值要保证唯一，最好使用当前Activity的名称
2. 可以添加不同类型的extra，可以在PostRouter中扩展



