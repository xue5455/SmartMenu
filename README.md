
##Inspired By [MeterialUp](https://material.uplabs.com/posts/options-floating-interaction)
 
 
###Screenshot
![Screenshot](https://raw.githubusercontent.com/xue5455/SmartMenu/master/screenshot/Gif.gif)

###How To Use
```
    <declare-styleable name="SmartMenu">
        <attr name="inner_padding" format="dimension" />  
        <attr name="outer_padding" format="dimension" />
        <attr name="smart_btn_size" format="dimension" />
        <attr name="vertical_padding" format="dimension" />
        <attr name="dot_radius" format="dimension" />
        <attr name="dot_distance" format="dimension" />
        <attr name="dot_color" format="color|reference" />
        <attr name="bg_color" format="color|reference" />
        <attr name="shadow_color" format="color|reference" />
    </declare-styleable>
```
>inner_padding —— 图标之间的距离

<br>

>outer_padding —— 左右边界距离图标的距离

<br>

>smart_btn_size —— 中间的按钮的尺寸

<br>

>vertical_padding —— 菜单的上边界和中间按钮的上边界的距离

<br>

>dot_radius —— 中间按钮的小圆点的半径

<br>

>dot_distance —— 左边的小圆点和右边的小圆点的距离(包括左右圆点的直径)

<br>

>dot_color —— 圆点的颜色

<br>

>bg_color —— 控件的背景颜色

<br>

>shadow_color —— 阴影的颜色



MenuAdapter

```
public class MenuAdapter extends BaseAdapter implements View.OnClickListener{

    private int[] images = new int[]{R.mipmap.icon_album,
            R.mipmap.icon_comment,
            R.mipmap.icon_draft,
            R.mipmap.icon_like};
    private ItemEventListener listener;

    public void setListener(ItemEventListener listener) {
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_menu, viewGroup, false);
        view.setOnClickListener(this);
        view.setTag(i);
        ImageView img = (ImageView) view.findViewById(R.id.image_view);
        img.setImageResource(images[i]);
        return view;
    }

    @Override
    public void onClick(View view) {
        if(listener!=null){
            listener.onEventNotify(view,(int)view.getTag());
        }
    }

}
```
通过给SmartMenu设置Adapter来添加icon，count数目需要是偶数，否则宽度计算会有错误。



###License

MIT License

Copyright (c) 2016 Jake

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
