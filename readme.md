# collection-utils

[![Download](https://img.shields.io/maven-central/v/com.guardanis/collection-utils-core)](https://search.maven.org/artifact/com.guardanis/collection-utils-core)

An assortment of modularized additives for collection-based layouts so I never have to create a class named `EndlessPullToRefreshStickyHeaderListView` or override an Adapter ever again.

The `ModularAdapter` system allows you to easily work with multiple View types in a generic way, without having to deal with all the bulky setup required. Each adapter is supplied with `ModuleBuilders` that create `AdapterViewModule` instances based on the type of Object to which they are assigned, allowing you to more elegantly separate your View data from application logic. 

Collection-based views have their own, separate, modular implementations (`ModularListView`, `ModularRecyclerView`, `ModularScrollView`, or `ModularGridView`, etc.) that can delegates touch and draw events to a `CollectionController`, which then delegates those events to its children `CollectionModule(s)` to accomplish specific tasks.  

# Installation

```groovy
repositories {
    mavenCentral()
}

ext { 
    collectionUtilsVersion = "4+"
}

dependencies {
    // Shared library for all projects
    implementation "com.guardanis:collection-utils-core:$collectionUtilsVersion"
    
    // Packages by View
    implementation "com.guardanis:collection-utils-listview:$collectionUtilsVersion"
    implementation "com.guardanis:collection-utils-recyclerview:$collectionUtilsVersion"
    implementation "com.guardanis:collection-utils-recyclerview-compat:$collectionUtilsVersion"
    implementation "com.guardanis:collection-utils-gridview:$collectionUtilsVersion"
    implementation "com.guardanis:collection-utils-scrollview:$collectionUtilsVersion"
    implementation "com.guardanis:collection-utils-viewpager:$collectionUtilsVersion"
}
```

As of version 4.0.0, Adapter/View implementations are packaged into separate modules. e.g.
* `ModularArrayAdapter`/`ModularListView` to `com.guardanis:collection-utils-listview`
* `ModularRecyclerAdapter`/`ModularRecyclerView` to `com.guardanis:collection-utils-recyclerview`
* `ModularPagerFragmentAdapter`/`ModularViewPager` to `com.guardanis:collection-utils-viewpager`

## ModularAdapter

Forget creating a new Adapter every time you want to create a new `ListView` or `RecyclerView`; let alone share similar items between them. 

Each `ModularAdapter` delegates all of that crappy responsibility to registered `AdapterViewModules` by their type, which act as both a ViewHolder and an interface for updating the Views with the correct data. This allows you to easily show many different layouts for one or more different types of data without having to do anything but setup an `AdapterViewModule` and register an appropriate `ModuleBuilder` for it.

To register different items to different `AdapterViewModules`, you can simply do something like:

```java
ModularArrayAdapter adapter = new ModularArrayAdapter(this);

// collection-utils 4.0.0+ only
adapter.registerModuleBuilder(
    ItemType1.class,
    new ModuleBuilder(() -> new ViewModule1(R.layout.list_type_1))
);

// collection-utils semi-compatible with <4.0.0 (removed ViewModule.class declaration)
adapter.registerModuleBuilder(
    ItemType2.class,
    new ModuleBuilder(
        R.layout.list_type_2,
        resId -> new ViewModule2(resId))
);
```

Where `ItemType{_}` and `ViewModule{_}` are the classes of the data in your adapter and their respective `AdapterViewModules`.

Now, let's say you want 1 data-type to match 2 alternating ViewHolders. That could be done by registering the `ModuleBuilderResolver` instead of just a builder:

```java
ModuleBuilder builder1 = new ModuleBuilder(
    ViewModule1.class,
    () -> new ViewModule1(R.layout.list_type_1)
);

ModuleBuilder builder2 = new ModuleBuilder(
    ViewModule2.class,
    () -> new ViewModule2(R.layout.list_type_2)
);

ModularArrayAdapter adapter = new ModularArrayAdapter(this)                
    .registerModuleBuilderResolver(
        ImageHolder1.class,
        new ModuleBuilderResolver(builder1, builder2) {
            public ModuleBuilder resolve(ModularArrayAdapter adapter, Object item, int position) {
                return position % 2 == 0
                    ? builder1
                    : builder2;
            }
        }
    );
```

Just a note: you must also supply the `ModuleBuilder` instances in the `ModuleBuilderResolver` constructor or else it won't know how to determine the true index of the view types for efficiently reusing layouts.

##### AdapterViewModule

The `AdapterViewModule` is the base class for use with the `ModuleBuilders` where you would setup and interact with your adapter's Views.

Currently, there are 3 base implementation classes: the `ListViewAdapterViewModule`, the `RecyclerViewAdapterViewModule`, and the `ViewPagerAdapterViewModule` for their respective Views. No, they are generally not compatible with one another, but can be nested together (see: collection-utils-recyclerview-compat).

`ListViewAdapterViewModule` example:

```java
public class SimpleViewModule extends ListViewAdapterViewModule<String> {

    private ImageView image;

    public SimpleViewModule(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void locateViewComponents(View convertView) {
        this.image = (ImageView) convertView.findViewById(R.id.some_view_id);
    }

    @Override
    public void updateView(ModularAdapter adapter, String item, int position) {
        new ImageRequest(adapter.getContext(), item)
            .setTargetView(image)
            .setFadeTransition()
            .execute();
    }
}
```
The same example, but for the `RecyclerViewAdapterViewModule`:

```java
public class SimpleViewModule extends RecyclerViewAdapterViewModule<String, SimpleViewHolder> {

    public SimpleViewModule(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected SimpleViewHolder buildViewHolder(View convertView) {
        return new SimpleViewHolder(convertView);
    }

    @Override
    public void updateView(ModularAdapter adapter, String item, int position) {
        new ImageRequest(adapter.getContext(), item)
            .setTargetView(getViewHolder().image)
            .setFadeTransition()
            .execute();
    }
}

public static class SimpleViewHolder extends RecyclerView.ViewHolder {
    
    ImageView image;

    public SimpleViewHolder(View convertView){
        super(convertView);
        
        this.image = (ImageView) convertView.findViewById(R.id.some_view_id);
    }
}
```

##### Actions : AdapterActionsManager

Since I want the modules to be as dumb and re-useable as possible, the `ModularAdapters` have a semi-typeless callback system allowing you to register, and trigger, callbacks with just keys and the values you want supplied with them. 

To register a callback with the adapter:

```java
adapter.registerCallback("key__my_item_clicked", item -> {
    Toast.makeText(context, item + " clicked!", Toast.LENGTH_SHORT)
        .show();
});
```

And then trigger it from within the `AdapterViewModule` on a click event

```java
someViewInstance.setOnClickListener(v ->
    adapter.triggerCallback("key__my_item_clicked", "some_item")
);
```

Types are enforced at runtime when accessed, so there is no compile-time safety for actions. Triggering an action that does not exist, or supplying an invalid type as an argument, will log a warning and do nothing. 

##### Properties : AdapterPropertiesManager

Again, we want to keep our Adapters dumb, but keep our `Activity` and `AdapterViewModules` informed of certain information. To do that, each `ModularAdapter` implementation integrates with a semi-typeless property storage and retrieval system.

To register a property with the adapter by key/value pair:

```java
adapter.setProperty("key__my_important_key", "some_important_value");
```

And then to access it again:

```java
String myImportantProperty = (String) adapter.getProperty("key__my_important_key");
```

Types are enforced at runtime when accessed, so there is no compile-time safety for properties. A `ClassCastException` will be thrown at runtime when expecting invalid types.


## CollectionModule

`CollectionModules` are the behavioral building blocks of this system. Each module is designed to separately handle a use case, and delegate the necessary events back (in a normalized way) that doesn't interfere with the default behavior of the ListView/GridView/RecyclerView/etc. 

#### EndlessModule

The EndlessModule triggers an `onNextPage()` callback when a user is approaching the end of the list. This is useful for pagination.

**Note**: You must manually call `EndlessModule.setLoading(false)` and/or `EndlessModule.setEndingReached(true)` when you have finished loading your next page of data or reached the end, respectively. Otherwise the next `onNextPage()` event won't be triggered.

#### StickyHeaderModule

When scrolling long lists, it's really nice to have sticky headers show you where you are. 

All you need to do is add a boolean tag to the View you create in your Adapter's overridden `getView(int, View, ViewGroup)` method, e.g.

```java
@Override
public View getView(int position, View convertView, ViewGroup parent){
    ViewHolder holder;
    if(convertView == null){
        holder = ....

        convertView.setTag(holder);
        convertView.setTag(R.integer.cu__sticky_header_tag_ref, true); // <-- here
    }
    ...
    return convertView;
}
```

#### ScrollEventModule

This will trigger a callback to `onScrolled(int distance)` as the collection-based View is scrolled. 

#### Pulling it all together

This will hopefully be the last time I ever write `EndlessPullToRefreshStickyHeaderListView` because that's what I'm implementing here:

```java
// Note: `PullToRefreshModule`/`RefreshEventListener` in example removed in version 2.3.0
public class TestActivity extends Activity implements EndlessEventListener, RefreshEventListener {

    private EndlessModule endlessModule;
    private PullToRefreshModule ptrModule;

    private int page = 1;

    @Override
    public void setup(Bundle savedInstance){
        setContentView(R.layout.some_layout);

        ModularListView listView = (ModularListView)findViewById(R.id.example_listview);
        listView.addHeaderView(activity.getLayoutInflater().inflate(R.layout.cu__pull_to_refresh, null, false));
        listView.registerModule(endlessModule = new EndlessModule(this));
        listView.registerModule(ptrModule = new PullToRefreshModule(this, this));
        listView.registerModule(ptrModule = new PullToRefreshModule(this, (ViewGroup) findViewbyId(R.id.your_view_in_layout), this)); // Or specify the View if you want it as an overlay
        listView.registerModule(new StickyHeaderModule());
    }

    @Override
    public void onNextPage(){
        page++;
        loadPageAsync();
    }

    @Override
    public void onRefresh(){
        page = 1;
        endlessModule.setLoading(true);
        endlessModule.setEndingReached(false);
        loadPageAsync();
    }

    protected void loadPageAsync(){
        // Load the page
    }
}
```

Now, assuming you remembered to call `convertView.setTag(R.integer.cu__sticky_header_tag_ref, true);` in your Adapter's `getView()` method for header items, you should, in fact, have an EndlessPullToRefreshStickyHeaderListView. Shit. I wrote it again.

Anyway... congratulations on modularizing your components for collection-based Views without having to worry about extending any adapter or View classes.

### Moved to MavenCentral

As of version 4.0.2, collection-utils will be hosted on MavenCentral. Versions 4.0.1 and below will remain on JCenter.