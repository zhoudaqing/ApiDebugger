package io.chengguo.apidebugger.ui.window;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.IdeFocusManager;
import com.intellij.ui.components.JBTextField;
import com.intellij.ui.tabs.TabInfo;
import io.chengguo.apidebugger.presenter.DebuggerSession;
import io.chengguo.apidebugger.ui.custom.JBDebuggerTab;
import io.chengguo.apidebugger.ui.iview.IHttpView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import static io.chengguo.apidebugger.engine.utils.ViewUtil.setCursor;

/**
 * Debugger inner widget
 * Created by fingerart on 17/2/28.
 */
public class InnerDebuggerWidget implements IHttpView, ActionListener {
    private Project mProject;
    private Disposable parent;
    public JPanel container;
    private JComboBox method;
    private JBTextField uri;
    private JButton send;
    private JBDebuggerTab reqTabs;
    private JBDebuggerTab resTabs;
    private JBTextField 文字JBTextField;
    private DebuggerSession mSession;
    private RequestBodyWidget requestBodyWidget;
    private RequestHeaderWidget requestHeaderWidget;

    public InnerDebuggerWidget(Project mProject, Disposable parent) {
        this.mProject = mProject;
        this.parent = parent;

        mSession = new DebuggerSession(this);

        setCursor(Cursor.HAND_CURSOR, method, send);
        send.addActionListener(this);
    }

    private void createUIComponents() {
        //Request
        reqTabs = new JBDebuggerTab(mProject, ActionManager.getInstance(), IdeFocusManager.getInstance(mProject), parent);

        TabInfo reqAuthorInfo = new TabInfo(new RequestAuthorizationWidget().container);
        reqAuthorInfo.setText("Authorization");
        reqTabs.addTab(reqAuthorInfo);

        requestHeaderWidget = new RequestHeaderWidget();
        TabInfo reqHeaderInfo = new TabInfo(requestHeaderWidget.container);
        reqHeaderInfo.setText("Header");
        reqTabs.addTab(reqHeaderInfo);

        requestBodyWidget = new RequestBodyWidget(mProject);
        TabInfo reqBodyInfo = new TabInfo(requestBodyWidget.container);
        reqBodyInfo.setText("Body");
        reqTabs.addTab(reqBodyInfo);

        //Response
        resTabs = new JBDebuggerTab(mProject, ActionManager.getInstance(), IdeFocusManager.getInstance(mProject), parent);

        TabInfo resBodyInfo = new TabInfo(new ResponseBodyWidget(mProject).container);
        resBodyInfo.setText("Body");
        resTabs.addTab(resBodyInfo);

        TabInfo resCookiesInfo = new TabInfo(new ResponseCookieWidget().container);
        resCookiesInfo.setText("Cookies");
        resTabs.addTab(resCookiesInfo);

        TabInfo resHeadersInfo = new TabInfo(new ResponseHeaderWidget().container);
        resHeadersInfo.setText("Headers");
        resTabs.addTab(resHeadersInfo);
    }

    @Override
    public String method() {
        return method.getSelectedItem().toString();
    }

    @Override
    public String url() {
        return uri.getText();
    }

    @Override
    public Map<String, String> headers() {
        return requestHeaderWidget.headers();
    }

    @Override
    public String bodyType() {
        return requestBodyWidget.bodyType();
    }

    @Override
    public Map<String, String> bodyFormData() {

        return null;
    }

    @Override
    public Map<String, String> bodyUrlencode() {
        return requestBodyWidget.bodyUrlencode();
    }

    @Override
    public Map<String, String> bodyRaw() {
        return null;
    }

    @Override
    public String bodyBinary() {
        return null;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "Send":
                mSession.execute();
                break;
        }
    }
}
