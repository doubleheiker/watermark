package com.watermark.main.configuration;

import java.io.Serializable;
import java.util.Deque;
import java.util.LinkedList;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.watermark.main.entity.UserInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.DefaultSessionKey;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;

public class KickoutSessionControlFilter extends AccessControlFilter {

    /** 踢出后到的地址 */
    private String kickoutUrl;
    /**  踢出之前登录的/之后登录的用户 默认踢出之前登录的用户 */
    private boolean kickoutAfter = true;

    /**  同一个帐号最大会话数 默认1 */
    private int maxSession = 1;
    private SessionManager sessionManager;
    private Cache<String, Deque<Serializable>> cache;

    public void setKickoutUrl(String kickoutUrl) {
        this.kickoutUrl = kickoutUrl;
    }

    public void setKickoutAfter(boolean kickoutAfter) {
        this.kickoutAfter = kickoutAfter;
    }

    public void setMaxSession(int maxSession) {
        this.maxSession = maxSession;
    }

    public void setSessionManager(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    public void setCacheManager(CacheManager cacheManager) {
        this.cache = cacheManager.getCache("shiro-activeSessionCache");
    }
    /**
     * 是否允许访问，返回true表示允许
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        return false;
    }
    /**
     * 表示访问拒绝时是否自己处理，如果返回true表示自己不处理且继续拦截器链执行，返回false表示自己已经处理了（比如重定向到另一个页面）。
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        Subject subject = getSubject(request, response);
        if(!subject.isAuthenticated()) {
            //如果没有登录，直接进行之后的流程
            return true;
        }

        Session session = subject.getSession();
        //这里获取的User是实体 因为我在 自定义ShiroRealm中的doGetAuthenticationInfo方法中
        //new SimpleAuthenticationInfo(user, password, getName()); 传的是 User实体 所以这里拿到的也是实体,如果传的是userName 这里拿到的就是userName
        String username = ((UserInfo) subject.getPrincipal()).getUsername();
        System.out.println("登录用户名"+username);
        Serializable sessionId = session.getId();
        System.out.println("sessionId:"+sessionId.toString());
        // 初始化用户的队列放到缓存里
        Deque<Serializable> deque = cache.get(username);
        if(deque == null) {
            deque = new LinkedList<Serializable>();
            cache.put(username, deque);
        }

        //如果队列里没有此sessionId，且用户没有被踢出；放入队列
        if(!deque.contains(sessionId) && session.getAttribute("kickout") == null) {
            deque.push(sessionId);
        }

        //如果队列里的sessionId数超出最大会话数，开始踢人
        while(deque.size() > maxSession) {
            Serializable kickoutSessionId = null;
            if(kickoutAfter) { //如果踢出后者
                kickoutSessionId = deque.getFirst();
                System.out.println(kickoutSessionId);
                kickoutSessionId = deque.removeFirst();
            } else { //否则踢出前者
                System.out.println("last:"+deque.getLast());
                kickoutSessionId = deque.removeLast();
            }
            System.out.println(22222);
            try {
                Session kickoutSession = sessionManager.getSession(new DefaultSessionKey(kickoutSessionId));
                if(kickoutSession != null) {
                    //设置会话的kickout属性表示踢出了
                    kickoutSession.setAttribute("kickout", true);
                }
            } catch (Exception e) {//ignore exception
                e.printStackTrace();
            }
        }
        System.out.println("并发登录");
        //如果被踢出了，直接退出，重定向到踢出后的地址
//    	System.out.println(session.getAttribute("kickout"));
        if (session.getAttribute("kickout") != null) {
            //会话被踢出了
            try {
                System.out.println("并发登录人数超过1人，登陆失败");
                subject.logout();
            } catch (Exception e) {
            }
            //throw new ConcurrentAccessException();

            WebUtils.issueRedirect(request, response, kickoutUrl);
            return false;
        }
        return true;
    }
}
