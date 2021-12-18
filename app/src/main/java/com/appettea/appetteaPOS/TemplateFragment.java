package com.appettea.appetteaPOS;


import androidx.fragment.app.Fragment;

/**
 * A template for all fragments, this adds a name for the fragment to be listed in menu
 * <p/>
 * Created by hcpl on 12/05/14.
 */
public class TemplateFragment extends Fragment {

    /**
     * @return the class name of the fragment, this can ben loaded in the drawer without need of a
     * special adapter
     */
    public String toString() {
        return getCurrentName();
    }

    public String getCurrentName(){

        return "Templete";
    }

    /**
     * retrieve a translated title for this fragment that can be used for display in menu
     *
     * @return
     */
    public int getTitleResourceId() {
        return R.string.empty_menu_item;
    }
}
