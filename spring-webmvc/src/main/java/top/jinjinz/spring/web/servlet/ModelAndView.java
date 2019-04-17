package top.jinjinz.spring.web.servlet;

import top.jinjinz.spring.context.ui.ModelMap;

/**
 * 返回的数据及视图
 * @author jinjin
 * @date 2019-04-17
 */
public class ModelAndView {

    private Object view;

    private ModelMap model;

    public Object getView() {
        return view;
    }

    public void setView(Object view) {
        this.view = view;
    }

    public ModelMap getModel() {
        return model;
    }

    public void setModel(ModelMap model) {
        this.model = model;
    }
}
