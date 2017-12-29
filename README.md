>这是一个快速实现下拉刷新，上拉翻页功能的框架，
该框架以PullToRefreshListView为基础，扩展了翻页的功能，具体使用方法如下：

---
gradle添加

在根gradle下添加

```
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
添加依赖

```
dependencies {
	       compile 'com.github.yourkkc:ykrefresh:v1.0.3'
	}
```

---

maven添加

```
<repositories>
		<repository>
		    <id>jitpack.io</id>
		    <url>https://jitpack.io</url>
		</repository>
</repositories>


<dependency>
	    <groupId>com.github.yourkkc</groupId>
	    <artifactId>ykrefresh</artifactId>
	    <version>v1.0.3</version>
</dependency>

```

---
使用步骤

```
public class MainActivity extends AppCompatActivity {


    private YKRefreshModelFactory<Test> modelFactory;
    private int count;
    private int everyPage = 40;//每页的条目数


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        PullToRefreshListView listView = (PullToRefreshListView) findViewById(R.id.listview);
        ImageView returnTop = (ImageView) findViewById(R.id.return_top);
        //初始化 Factory 参数 listview ,adapter,结束布局，activity,返回顶部按钮(没有返回顶部功能可以不写)
        modelFactory = new YKRefreshModelFactory<Test>(
                listView,
                new TestAdapter(MainActivity.this),
                R.layout.list_foot_view,
                MainActivity.this,
                returnTop);

        //设置每一页的条目数
        modelFactory.setEveryPage(everyPage);//默认20
        //设置载入提示消失时长--时长太短的话可能存在消除不掉的现象 默认200ms
        modelFactory.setDelayDismissTip(200);
        //设置返回顶部按钮的动画方式
        modelFactory.setAnimStyle(AnimUtils.TYPE_SCALE);//缩放  默认
//        modelFactory.setAnimStyle(AnimUtils.TYPE_ALPHA);//透明
        //设置返回顶部按钮的动画时长
        modelFactory.setAnimDuration(1000);//默认1000
        //设置查询的方法
        modelFactory.setmQueryListener(new YKQueryListener() {
            @Override
            public void query() {
                //查询方法
                //查询到总条目之后--设置数量
                modelFactory.setDataCount(200);
                //查询到新页数的数据之后  进行跳页
                modelFactory.nextPage();
                //假设新条目集合在这里
                List<Test> data = queryData();
                //添加到原条目中
                for(Test w :data){
                    modelFactory.getData().add(w);
                }
                //刷新数据即可
                modelFactory.flushData();
            }
        });
        //设置单击事件
        modelFactory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        //获得单击条目
                        Test t = (Test) adapterView.getAdapter().getItem(i);
                        Toast.makeText(MainActivity.this, "单击了"+t.getContent(), Toast.LENGTH_SHORT).show();
            }
        });
        //设置长按事件
        modelFactory.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                //获得单击条目
                Test t = (Test) adapterView.getAdapter().getItem(i);
                Toast.makeText(MainActivity.this, "长按了"+t.getContent(), Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        modelFactory.getmQueryListener().query();//进行第一次查询
    }

    public List<Test> queryData() {

        List<Test> data = new ArrayList<Test>();

        if(modelFactory.getData().size()==0){
            count = 0;
        }
        for(int i=count;i<count+everyPage;i++){
            data.add(new Test("条目"+(i+1)));
        }
        count+=everyPage;
        return data;
    }
}
//适配器类
class TestAdapter extends YKBaseAdapter<Test> {


    public TestAdapter(Context context, List<Test> data) {
        super(context, data);
    }

    public TestAdapter(Context context) {
        super(context);
    }

    @Override
    public Test getItem(int i) {
        return data.get(i);
    }

    @Override
    public int getCount() {
        return data.size();
    }



    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {

        Holder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.listview_item, null);
            holder = new Holder();
            holder.content = (TextView) convertView.findViewById(R.id.item_name);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        holder.content.setText(data.get(i).getContent());
        return convertView;
    }
    static class Holder{
        TextView content;
    }
}


```
---
XML布局
```
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    tools:context="com.yourkkc.refresh.activity.MainActivity">
    <FrameLayout
        android:layout_width="match_parent"
        android:layout_height="0dp"
        android:layout_weight="1">
        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:orientation="vertical">

            <com.handmark.pulltorefresh.library.PullToRefreshListView
                android:id="@+id/listview"
                android:layout_width="match_parent"
                android:layout_height="match_parent">
            </com.handmark.pulltorefresh.library.PullToRefreshListView>
        </LinearLayout>
        <ImageView
            android:id="@+id/return_top"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_gravity="bottom"
            android:src="@mipmap/returntop"
            android:visibility="invisible" />
    </FrameLayout>
</LinearLayout>

```
