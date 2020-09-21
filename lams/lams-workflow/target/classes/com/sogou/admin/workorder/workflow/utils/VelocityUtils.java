package com.sogou.admin.workorder.workflow.utils;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;

public class VelocityUtils {

    private static VelocityEngine engine = null;
    private static final String ENCODING = "UTF-8";


    static {
        engine = new VelocityEngine();
        engine.setProperty(Velocity.RESOURCE_LOADER, "class");
        engine.setProperty("class.resource.loader.class",
                "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");

    }

    /**
     * 根据模板文件和数据生成渲染后的文件
     *
     * @param tplFilePath    模板文件路径
     * @param targetFilePath 结果文件路径
     * @param dataMap        数据集
     */
    public static void fill(String tplFilePath, String targetFilePath, Map<String, Object> dataMap) {
        OutputStreamWriter writer = null;
        try {
            engine.init();
            Template template = engine.getTemplate(tplFilePath, ENCODING);
            VelocityContext context = new VelocityContext(dataMap);

            writer = new OutputStreamWriter(new FileOutputStream(targetFilePath), ENCODING);
            if (template != null) {
                template.merge(context, writer);
            }
            writer.flush();
        } catch (Exception e) {
            throw new RuntimeException("Error when rendering by velocity.", e);
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (Exception e) {
                throw new RuntimeException("Error when closing stream.", e);
            }
        }
    }

    /**
     * 根据模板文件路径和数据集，返回渲染后的字符串
     *
     * @param tplFilePath
     * @param dataMap
     * @return
     */
    public static String fillWithNormalEngine(String tplFilePath, Map<String, Object> dataMap) {
        try {
            engine.init();
            Template template = engine.getTemplate(tplFilePath, ENCODING);
            VelocityContext context = new VelocityContext(dataMap);
            context.put("StringUtils", new StringUtils());
            StringWriter writer = new StringWriter();
            template.merge(context, writer);
            return writer.toString();
        } catch (Exception e) {
            throw new RuntimeException("Error when rendering by velocity.", e);
        }
    }
}
