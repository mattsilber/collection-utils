# collection-utils

[![Download](https://api.bintray.com/packages/mattsilber/maven/collection-utils/images/download.svg) ](https://bintray.com/mattsilber/maven/collection-utils/_latestVersion)

An assortment of modularized additives for collection-based layouts so I never have to create a class named `EndlessPullToRefreshStickyHeaderListView` ever again.

The idea is simple: Have a `ModularListView`, `ModularRecyclerView`, `ModularScrollView`, or `ModularGridView` that can delegates touch and draw events to a `CollectionController`, which then delegates those events to its child `CollectionModule(s)` to accomplish a specific task.

On top of that, the `ModularAdapter` system allows you to easily work with multiple View types in a generic way, without having to deal with all the bulky setup required. Each adapter is supplied with `ModuleBuilders` mapped to the types of Objects they should handle, allowing you to more elegantly separate your View data from application logic. The `ModularAdapters` also come with a semi-typeless `Callback<T>` and property system, letting your modules remain ignorant of their true use, and thus, making them more re-useable.

# Installation

```groovy
repositories {
    jcenter()
}

dependencies {
    compile('com.guardanis:collection-utils:3.0.0')
}
```

## CollectionModule

`CollectionModules` are the behavioral building blocks of this system. Each module is designed to separately handle a use case, and delegate the necessary events back in a normalized way that doesn't interfere with the default behavior of the ListView/GridView/RecyclerView/etc. I've already implemented several modules include PullToRefresh, StickyHeaders, ScrollEvent, and Endless modules (see below for details). If you come up with others, please feel free to share and make a pull request.

### Currently Implemented ListView modules

#### EndlessModule

The EndlessModule triggers an `onNextPage()` callback when a user is approaching the end of the list.

**Note**: You must manually call `EndlessModule.setLoading(false)` and/or `EndlessModule.setEndingReached(true)` when you have finished loading your next page of data or reached the end, respectively. Otherwise the next `onNextPage()` event won't be triggered.

#### PullToRefreshModule

Removed in version 2.3.0

A simple pull to refresh system. Pulling down from the top of a list will pull cause the header to expand. When released passed the threshold, it will trigger a loading animation and a callback to `onRefresh()`, allowing you to update the data.

*Important*: If you don't specify the container in the constructor of the `PullToRefreshModule`, then you must inflate `R.layout.cu__pull_to_refresh` as a header View for the ModularListView before adding the PullToRefreshModule. The former would allow you to create the PTR module as an overlay above your ListView or ScrollView.

If you would like to override the drawables used (because they're horrible placeholders, so why wouldn't you?), just override `R.drawable.cu__ptr_loading_image` and `R.drawable.cu__ptr_pulling_image` or set the images into the Views after they have been inflated.

###### Image Delegates

As of version 1.0.7, the PTR-related ImageViews will delegate their update/drawing calls to either a `PulledImageDelegate` or `LoadingImageDelegate` so that the behavior of the drawing can be changed at will.

#### StickyHeaderModule

When scrolling long data-separated lists (or any grouped list, for that matter), it's really nice to have sticky headers show you where you are. This will help you do that in the case where the entire View should be treated as a header.

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

I very often find myself needing to know when, and how far, a `ListView` is being scrolled. This will trigger a callback to `onScrolled(int distance)` as that happens.

#### Pulling it all together

This will hopefully be the last time I ever write `EndlessPullToRefreshStickyHeaderListView` because that's what I'm implementing here:

```java
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

Anyway, this allows you to create custom components for a ListView without having to worry about extending the class.

## ModularAdapter

This adapter has support for delegating different items in the adapter to one or more different `AdapterViewModules`, which act as both a ViewHolder and an interface for updating the Views in said holder with the correct data. This allows you to easily show many different layouts for one or more different types of data without having to do anything but setup an `AdapterViewModule` and register a `ModuleBuilder` for it.

To register different items to different `AdapterViewModules`, you can do something like:

```java
ModularArrayAdapter adapter = new ModularArrayAdapter(this)
        .registerModuleBuilder(ItemType1.class,
                new ModuleBuilder(R.layout.list_type_1,
                        ViewModule1.class,
                        resId -> new ViewModule1(resId)))
        .registerModuleBuilder(ItemType2.class,
                new ModuleBuilder(R.layout.list_type_2
                        ViewModule2.class,
                        resId -> new ViewModule2(resId)));
```

Where ItemType{_} and ViewModule{_} are the classes of the data in your adapter and their respective `AdapterViewModules`.

Now, let's say you want 1 data-type to match 2 alternating ViewHolders. That could be done by registering the `ModuleBuilderResolver` instead of just a builder:

```java
ModuleBuilder builder1 = new ModuleBuilder(R.layout.list_type_1,
        ViewModule1.class,
        resId -> new ViewModule1(resId));

ModuleBuilder builder2 = new ModuleBuilder(R.layout.list_type_2,
        ViewModule2.class,
        resId -> new ViewModule2(resId));

ModularArrayAdapter adapter = new ModularArrayAdapter(this)                
        .registerModuleBuilderResolver(ImageHolder1.class,
                new ModuleBuilderResolver(builder1, builder2) {
                    public ModuleBuilder resolve(ModularArrayAdapter adapter, Object item, int position) {
                        return position % 2 == 0
                                ? builder1
                                : builder2;
                    }
                });
```

Just a note: you must also supply the `ModuleBuilder` instances in the `ModuleBuilderResolver` constructor or else it won't know how to determine the true indeces of the view types for efficiently reusing layouts.

##### AdapterViewModule

The `AdapterViewModule` is the base class for use with the modular builders. Currently, there are 3 base implementation classes: the `ListViewModule`, the `RecyclerViewModule`, and the `PagerViewModule` for their respective Views. No, they are generally not compatible with one another, but can be nested together.

`ListViewModule` example:

```java
public class SimpleViewModule extends ListViewModule<String> {

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
The same example, but for the `RecyclerViewModule`:

```java
public class SimpleViewModule extends RecyclerViewModule<String, SimpleViewHolder> {

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

##### Callbacks

Since I want the modules to be as dumb and re-useable as possible, the `ModularAdapters` have a semi-typeless callback system allowing you to register, and trigger, callbacks with just keys and the values you want supplied with them. Granted, this removes a lot of compile-time type-safety and can make it more difficult to track down issues, but there are tradeoffs with everything.

Ie. register a callback with the adapter

```java
adapter.registerCallback("key__my_item_clicked", item -> 
    Toast.makeText(context, item + " clicked!", Toast.LENGTH_SHORT).show());
```

And then trigger it from with the ViewModule on a click event

```java
getConvertView().setOnClickListener(v ->
    adapter.triggerCallback("key__my_item_clicked", item));
```

Types can be defined at the registration-level (e.g. `adapter.registerCallback(String, Callback<T>)`, and are enforced when triggered, but there is no compile-time safety (since there is no hard link between the two).

### ListUtils

As of Version 3.0.0, ListUtils will reside in the Legacy extensions since Kotlin/J8 basically make them useless.

### Legacy Extensions

```groovy
dependencies {
    compile('com.guardanis:collection-utils-legacy:3.0.0')
}
```
