# collection-utils

An assortment of modularized additives for collection-based layouts so I never have to create a class named EndlessPullToRefreshStickyHeaderListView ever again.

The idea is simple: Have a ModularizedListView that can delegates touch and draw events to a CollectionController, which then delegates those events to its child CollectionModule(s) to accomplish a specific task. 

# Installation

```
    repositories {
        jcenter()
    }

    dependencies {
        compile('com.guardanis:collection-utils:1.0.6')
    }
```

# CollectionModule

I've already implemented several modules, include PullToRefresh, StickyHeaders, ScrollEvent, and Endless modules (see below for details). If you come up with others, please feel free to share and make a pull request.

## Currently Implemented ListView modules

### EndlessModule

The EndlessModule triggers a callback onNextPage() when a user is approaching the end of the list.

**Note**: You must manually call endlessModule.setLoading(false) and/or endlessModule.setEndingReached(true) when you have finished loading your next page of data or reached the end, respectively. Otherwise the next onNextPage() event won't be triggered.

### PullToRefreshModule

A simple pull to refresh system. Pulling down from the top of a list will pull cause the header to expand. When released passed the threshold, it will trigger a loading animation and a callback to **onRefresh()**, allowing you to update the data.

*Important*: Inflate R.layout.cu__pull_to_refresh as a header View for the ModularListView before adding the PullToRefreshModule.

If you would like to override the drawables used (because, why wouldn't you?), just override *R.drawable.cu__ptr_loading_image* and *R.drawable.cu__ptr_pulling_image*.

### StickyHeaderModule

When scrolling long data-separated lists (or any grouped list, for that matter), it's really nice to have sticky headers show you where you are. This will help you do that.

All you need to do is add a boolean tag to the View you create in your Adapter's overridden **getView(int, View, ViewGroup)** method, e.g.

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

### ScrollEventModule

I very often find myself needing to know when, and how far, a ListView is being scrolled. This will trigger a callback to **onScrolled(int distance)** as that happens.

### Pulling it all together

This will hopefully be the last time I ever write EndlessPullToRefreshStickyHeaderListView because that's what I'm implementing here:

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
            listView.registerModule(ptrModule = new PullToRefreshModule(this, this);
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
    
Now, assuming you remembered to call **convertView.setTag(R.integer.cu__sticky_header_tag_ref, true);** in your Adapter's **getView()** method for header items, you should, in fact, have an EndlessPullToRefreshStickyHeaderListView. Shit. I wrote it again.

Anyway, this allows you to create custom components for a ListView without having to worry about extending the class.

