package com.appmonitor.utils;

import com.appmonitor.apprestatpic.service.AppDeepTimeService;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Created by xuds on 2017/6/17.
 */
@Component //此处注解不能省却（0）
public class RenderTimeByUrl {

    @Autowired
    private AppDeepTimeService appDeepTimeService;
    private static RenderTimeByUrl opeLogUtils;

    public void setUserInfo(AppDeepTimeService appDeepTimeService) {
        this.appDeepTimeService = appDeepTimeService;
    }

    @PostConstruct
    public void init() {
        opeLogUtils = this;
        opeLogUtils.appDeepTimeService = this.appDeepTimeService;
    }

    private final static int maxLoadTime = 1000;
    static BASE64Encoder encoder = new sun.misc.BASE64Encoder();
    static BASE64Decoder decoder = new sun.misc.BASE64Decoder();

    public static String getRenderTime(String appId, String appName, String urlStr) {
        System.setProperty("webdriver.chrome.driver", "D:\\driver\\chromedriver.exe");
        // 创建一个 Chrome 的浏览器实例
        String time = "0";
        String binary = "";
        try {
            ChromeDriverService service = new ChromeDriverService.Builder().usingDriverExecutable(new File("D:\\driver\\chromedriver.exe")).usingAnyFreePort().build();
            service.start();
           /* ChromeOptions options = new ChromeOptions();
            //通过配置参数禁止data;的出现
            options.addArguments("--user-data-dir=C:/Users/Administrator/AppData/Local/Google/Chrome/User Data/Default");
            //通过配置参数删除“您使用的是不受支持的命令行标记：--ignore-certificate-errors。稳定性和安全性会有所下降。”提示
            options.addArguments("--start-maximized", "allow-running-insecure-content", "--test-type");
            WebDriver driver = new ChromeDriver(options);*/

            WebDriver driver = new ChromeDriver();

            driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
            //设置浏览器大小
            //将浏览器设置为最大化的状态
            driver.manage().window().maximize();
            Long begintime = System.currentTimeMillis();
            System.out.println(appName + "开始时间===" + System.currentTimeMillis());
            driver.navigate().to(urlStr);
            //指定了OutputType.FILE做为参数传递给getScreenshotAs()方法，其含义是将截取的屏幕以文件形式返回。
            File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
//            FileUtils.copyFile(srcFile, new File("D:\\Users\\Desktop\\"+appId+".png"));
            BufferedImage bi;
            try {
                bi = ImageIO.read(srcFile);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(bi, "png", baos);
                byte[] bytes = baos.toByteArray();
                binary = Base64.encodeBase64String(bytes);
                binary = encoder.encodeBuffer(bytes).trim();
                opeLogUtils.appDeepTimeService.insertPicToBinary(appId, appName, binary);
            } catch (IOException e) {
                e.printStackTrace();
            }
//            try {
//                byte[] bytes1 = decoder.decodeBuffer(binary);
//                ByteArrayInputStream bais = new ByteArrayInputStream(bytes1);
//                BufferedImage bi1 =ImageIO.read(bais);
//                File w2 = new File("D:\\Users\\Desktop2\\"+appId+".png");//可以是jpg,png,gif格式
//                ImageIO.write(bi1, "jpg", w2);//不管输出什么格式图片，此处不需改动
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
            Long endtime = System.currentTimeMillis();
            System.out.println(appName + "结束时间===" + System.currentTimeMillis());
            System.out.println(appName + "==" + urlStr + "----的渲染时间===" + (endtime - begintime));
            time = String.valueOf((endtime - begintime));
            driver.quit();//quit方法是完全关闭浏览器方法
            service.stop();
            return time;
        } catch (Exception ex) {
//            ex.printStackTrace();
            System.out.println("渲染时间超过30s！");
        } finally {
            return time;
        }
    }


    /**
     * rewrite the get method, adding user defined log</BR>
     * 地址跳转方法，使用WebDriver原生get方法，加入失败重试的次数定义。
     *
     * @param url         the url you want to <span id="2_nwp" style="width: auto; height: auto; float: none;"><a target=_blank id="2_nwl" href="http://cpro.baidu.com/cpro/ui/uijs.php?adclass=0&app_id=0&c=news&cf=1001&ch=0&di=128&fv=11&is_app=0&jk=69829344341ee6cd&k=open&k0=open&kdi0=0&luki=2&n=10&p=baidu&q=v77y4_cpr&rb=0&rs=1&seller_id=1&sid=cde61e3444938269&ssp2=1&stid=0&t=tpclicked3_hc&td=2102575&tu=u2102575&u=http%3A%2F%2Fwww%2Eylzx8%2Ecn%2Fzonghe%2Fopen%2Dsource%2F247951%2Ehtml&urlid=0" target="_blank" mpid="2" style="color: rgb(1, 70, 108); text-decoration: none;"><span style="color: rgb(0, 0, 255); width: auto; height: auto;">open</span></a></span>.
     * @param actionCount retry times when load timeout occuers.
     * @throws RuntimeException
     */
    public static void get(String url, int actionCount) {
        boolean inited = false;
        int index = 0, timeout = 10;
        while (!inited && index < actionCount) {
            timeout = (index == actionCount - 1) ? maxLoadTime : 10;//最后一次跳转使用最大的默认超时时间
            inited = navigateAndLoad(url, timeout);
            index++;
        }
        if (!inited && index == actionCount) {//最终跳转失败则抛出运行时异常，退出运行
            throw new RuntimeException("can not get the url [" + url + "] after retry " + actionCount + "times!");
        }
    }

    /**
     * rewrite the get method, adding user defined log</BR>
     * 地址跳转方法，使用WebDriver原生get方法，默认加载超重试【1】次。
     *
     * @param url the url you want to open.
     * @throws RuntimeException
     */
    public static void getrr(String url) {
        get(url, 8);
    }

    /**
     * judge if the url has navigate and page load completed.</BR>
     * 跳转到指定的URL并且返回是否跳转完整的结果。
     *
     * @param url     the url you want to open.
     * @param timeout the timeout for page load in seconds.
     * @return if page load completed.
     */
    public static boolean navigateAndLoad(String url, int timeout) {
        System.setProperty("webdriver.chrome.driver", "E:\\driver\\chromedriver.exe");
        // 创建一个 ChromeDriver 的接口，用于连接 Chrome
        // 创建一个 Chrome 的浏览器实例

        try {
            WebDriver driver = new ChromeDriver();
            driver.manage().timeouts().pageLoadTimeout(timeout, TimeUnit.SECONDS);
            driver.get(url);
            return true;//跳转并且加载页面成功在返回true
        } catch (Exception e) {
            throw new RuntimeException(e);//抛出运行时异常，退出运行
        } finally {
//            driver.manage().timeouts().pageLoadTimeout(maxLoadTime, TimeUnit.SECONDS);
        }
    }
}
