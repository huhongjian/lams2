package com.bupt.lams.utils;

import com.bupt.lams.model.Asset;
import com.bupt.lams.model.Order;
import com.bupt.lams.model.PurchaseOrder;
import org.apache.poi.hpsf.DocumentSummaryInformation;
import org.apache.poi.hpsf.SummaryInformation;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * 文档工具类
 */
public class POIUtils {

    /**
     * 入库记录转excel
     *
     * @param list
     * @return
     */
    public static ResponseEntity<byte[]> assetIn2Excel(List<Order> list) {
        //1. 创建一个 Excel 文档
        HSSFWorkbook workbook = new HSSFWorkbook();
        //2. 创建文档摘要
        workbook.createInformationProperties();
        //3. 获取并配置文档信息
        DocumentSummaryInformation docInfo = workbook.getDocumentSummaryInformation();
        //文档类别
        docInfo.setCategory("入库记录信息");
        //文档管理员
        docInfo.setManager("admin");
        //设置公司信息
        docInfo.setCompany("bupt");
        //4. 获取文档摘要信息
        SummaryInformation summInfo = workbook.getSummaryInformation();
        //文档标题
        summInfo.setTitle("入库记录信息表");
        //文档作者
        summInfo.setAuthor("lams");
        // 文档备注
        summInfo.setComments("本文档由 lams 提供");
        //5. 创建样式
        //创建标题行的样式
        HSSFCellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.YELLOW.index);
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        HSSFCellStyle dateCellStyle = workbook.createCellStyle();
        dateCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy"));
        HSSFSheet sheet = workbook.createSheet("入库记录信息表");
        //设置列的宽度
        sheet.setColumnWidth(0, 10 * 256);
        sheet.setColumnWidth(1, 10 * 256);
        sheet.setColumnWidth(2, 10 * 256);
        sheet.setColumnWidth(3, 10 * 256);
        sheet.setColumnWidth(4, 10 * 256);
        sheet.setColumnWidth(5, 10 * 256);
        sheet.setColumnWidth(6, 15 * 256);
        sheet.setColumnWidth(7, 20 * 256);
        sheet.setColumnWidth(8, 15 * 256);
        sheet.setColumnWidth(9, 30 * 256);
        sheet.setColumnWidth(10, 10 * 256);
        //6. 创建标题行
        HSSFRow r0 = sheet.createRow(0);
        HSSFCell c0 = r0.createCell(0);
        c0.setCellValue("采购单号");
        c0.setCellStyle(headerStyle);
        HSSFCell c1 = r0.createCell(1);
        c1.setCellStyle(headerStyle);
        c1.setCellValue("资产编号");
        HSSFCell c2 = r0.createCell(2);
        c2.setCellStyle(headerStyle);
        c2.setCellValue("类型");
        HSSFCell c3 = r0.createCell(3);
        c3.setCellStyle(headerStyle);
        c3.setCellValue("品牌");
        HSSFCell c4 = r0.createCell(4);
        c4.setCellStyle(headerStyle);
        c4.setCellValue("状态");
        HSSFCell c5 = r0.createCell(5);
        c5.setCellStyle(headerStyle);
        c5.setCellValue("价格（元）");
        HSSFCell c6 = r0.createCell(6);
        c6.setCellStyle(headerStyle);
        c6.setCellValue("申请人");
        HSSFCell c7 = r0.createCell(7);
        c7.setCellStyle(headerStyle);
        c7.setCellValue("申请人邮箱");
        HSSFCell c8 = r0.createCell(8);
        c8.setCellStyle(headerStyle);
        c8.setCellValue("申请人电话");
        HSSFCell c9 = r0.createCell(9);
        c9.setCellStyle(headerStyle);
        c9.setCellValue("申请理由");
        HSSFCell c10 = r0.createCell(10);
        c10.setCellStyle(headerStyle);
        c10.setCellValue("申请时间");
        int cnt = 0;
        for (int i = 0; i < list.size(); i++) {
            Order order = list.get(i);
            List<Asset> assetList = order.getAssetList();
            for (int j = 0; j < assetList.size(); j++) {
                Asset asset = assetList.get(j);
                HSSFRow row = sheet.createRow(++cnt);
                row.createCell(0).setCellValue(order.getId());
                row.createCell(1).setCellValue(asset.getId());
                row.createCell(2).setCellValue(asset.getType());
                row.createCell(3).setCellValue(asset.getBrand());
                row.createCell(4).setCellValue(order.getStatusName());
                row.createCell(5).setCellValue(asset.getPrice());
                row.createCell(6).setCellValue(order.getUser().getName());
                row.createCell(7).setCellValue(order.getUserEmail());
                row.createCell(8).setCellValue(order.getUser().getPhone());
                row.createCell(9).setCellValue(order.getReason());
                HSSFCell cell4 = row.createCell(10);
                cell4.setCellStyle(dateCellStyle);
                cell4.setCellValue(order.getCreateTime());
            }
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.setContentDispositionFormData("attachment", new String("入库记录信息表.xls".getBytes("UTF-8"), "ISO-8859-1"));
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            workbook.write(baos);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(baos.toByteArray(), headers, HttpStatus.CREATED);
    }

    /**
     * 出库记录转excel
     *
     * @param list
     * @return
     */
    public static ResponseEntity<byte[]> assetOut2Excel(List<Order> list) {
        //1. 创建一个 Excel 文档
        HSSFWorkbook workbook = new HSSFWorkbook();
        //2. 创建文档摘要
        workbook.createInformationProperties();
        //3. 获取并配置文档信息
        DocumentSummaryInformation docInfo = workbook.getDocumentSummaryInformation();
        //文档类别
        docInfo.setCategory("出库记录信息");
        //文档管理员
        docInfo.setManager("admin");
        //设置公司信息
        docInfo.setCompany("bupt");
        //4. 获取文档摘要信息
        SummaryInformation summInfo = workbook.getSummaryInformation();
        //文档标题
        summInfo.setTitle("出库记录信息表");
        //文档作者
        summInfo.setAuthor("lams");
        // 文档备注
        summInfo.setComments("本文档由 lams 提供");
        //5. 创建样式
        //创建标题行的样式
        HSSFCellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.YELLOW.index);
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        HSSFCellStyle dateCellStyle = workbook.createCellStyle();
        dateCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy"));
        HSSFSheet sheet = workbook.createSheet("出库记录信息表");
        //设置列的宽度
        sheet.setColumnWidth(0, 10 * 256);
        sheet.setColumnWidth(1, 10 * 256);
        sheet.setColumnWidth(2, 10 * 256);
        sheet.setColumnWidth(3, 10 * 256);
        sheet.setColumnWidth(4, 10 * 256);
        sheet.setColumnWidth(5, 15 * 256);
        sheet.setColumnWidth(6, 10 * 256);
        sheet.setColumnWidth(7, 20 * 256);
        sheet.setColumnWidth(8, 15 * 256);
        sheet.setColumnWidth(9, 30 * 256);
        sheet.setColumnWidth(10, 10 * 256);
        sheet.setColumnWidth(11, 10 * 256);
        //6. 创建标题行
        HSSFRow r0 = sheet.createRow(0);
        HSSFCell c0 = r0.createCell(0);
        c0.setCellValue("借用单号");
        c0.setCellStyle(headerStyle);
        HSSFCell c1 = r0.createCell(1);
        c1.setCellStyle(headerStyle);
        c1.setCellValue("资产编号");
        HSSFCell c2 = r0.createCell(2);
        c2.setCellStyle(headerStyle);
        c2.setCellValue("类型");
        HSSFCell c3 = r0.createCell(3);
        c3.setCellStyle(headerStyle);
        c3.setCellValue("品牌");
        HSSFCell c4 = r0.createCell(4);
        c4.setCellStyle(headerStyle);
        c4.setCellValue("状态");
        HSSFCell c5 = r0.createCell(5);
        c5.setCellStyle(headerStyle);
        c5.setCellValue("预计归还时间");
        HSSFCell c6 = r0.createCell(6);
        c6.setCellStyle(headerStyle);
        c6.setCellValue("负责人");
        HSSFCell c7 = r0.createCell(7);
        c7.setCellStyle(headerStyle);
        c7.setCellValue("负责人邮箱");
        HSSFCell c8 = r0.createCell(8);
        c8.setCellStyle(headerStyle);
        c8.setCellValue("负责人电话");
        HSSFCell c9 = r0.createCell(9);
        c9.setCellStyle(headerStyle);
        c9.setCellValue("申请理由");
        HSSFCell c10 = r0.createCell(10);
        c10.setCellStyle(headerStyle);
        c10.setCellValue("申请时间");
        HSSFCell c11 = r0.createCell(11);
        c11.setCellStyle(headerStyle);
        c11.setCellValue("入库时间");
        int cnt = 0;
        for (int i = 0; i < list.size(); i++) {
            Order order = list.get(i);
            List<Asset> assetList = order.getAssetList();
            for (int j = 0; j < assetList.size(); j++) {
                Asset asset = assetList.get(j);
                HSSFRow row = sheet.createRow(++cnt);
                row.createCell(0).setCellValue(order.getId());
                row.createCell(1).setCellValue(asset.getId());
                row.createCell(2).setCellValue(asset.getType());
                row.createCell(3).setCellValue(asset.getBrand());
                row.createCell(4).setCellValue(order.getStatusName());
                HSSFCell cell5 = row.createCell(5);
                cell5.setCellStyle(dateCellStyle);
                cell5.setCellValue(order.getExpireTime());
                row.createCell(6).setCellValue(order.getUser().getName());
                row.createCell(7).setCellValue(order.getUserEmail());
                row.createCell(8).setCellValue(order.getUser().getPhone());
                row.createCell(9).setCellValue(order.getReason());
                HSSFCell cell10 = row.createCell(10);
                cell10.setCellStyle(dateCellStyle);
                cell10.setCellValue(order.getCreateTime());
                HSSFCell cell11 = row.createCell(11);
                cell11.setCellStyle(dateCellStyle);
                cell11.setCellValue(asset.getReadyDate());
            }
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.setContentDispositionFormData("attachment", new String("出库记录信息表.xls".getBytes("UTF-8"), "ISO-8859-1"));
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            workbook.write(baos);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(baos.toByteArray(), headers, HttpStatus.CREATED);
    }

    /**
     * 资产信息记录转excel
     *
     * @param list
     * @return
     */
    public static ResponseEntity<byte[]> assetInfo2Excel(List<Asset> list) {
        //1. 创建一个 Excel 文档
        HSSFWorkbook workbook = new HSSFWorkbook();
        //2. 创建文档摘要
        workbook.createInformationProperties();
        //3. 获取并配置文档信息
        DocumentSummaryInformation docInfo = workbook.getDocumentSummaryInformation();
        //文档类别
        docInfo.setCategory("资产信息");
        //文档管理员
        docInfo.setManager("admin");
        //设置公司信息
        docInfo.setCompany("bupt");
        //4. 获取文档摘要信息
        SummaryInformation summInfo = workbook.getSummaryInformation();
        //文档标题
        summInfo.setTitle("资产信息表");
        //文档作者
        summInfo.setAuthor("lams");
        // 文档备注
        summInfo.setComments("本文档由 lams 提供");
        //5. 创建样式
        //创建标题行的样式
        HSSFCellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.YELLOW.index);
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        HSSFCellStyle dateCellStyle = workbook.createCellStyle();
        dateCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy"));
        HSSFSheet sheet = workbook.createSheet("资产信息表");
        //设置列的宽度
        sheet.setColumnWidth(0, 10 * 256);
        sheet.setColumnWidth(1, 10 * 256);
        sheet.setColumnWidth(2, 10 * 256);
        sheet.setColumnWidth(3, 10 * 256);
        sheet.setColumnWidth(4, 10 * 256);
        sheet.setColumnWidth(5, 10 * 256);
        sheet.setColumnWidth(6, 10 * 256);
        //6. 创建标题行
        HSSFRow r0 = sheet.createRow(0);
        HSSFCell c0 = r0.createCell(0);
        c0.setCellValue("资产编号");
        c0.setCellStyle(headerStyle);
        HSSFCell c1 = r0.createCell(1);
        c1.setCellStyle(headerStyle);
        c1.setCellValue("状态");
        HSSFCell c2 = r0.createCell(2);
        c2.setCellStyle(headerStyle);
        c2.setCellValue("类型");
        HSSFCell c3 = r0.createCell(3);
        c3.setCellStyle(headerStyle);
        c3.setCellValue("品牌");
        HSSFCell c4 = r0.createCell(4);
        c4.setCellStyle(headerStyle);
        c4.setCellValue("价格（元）");
        HSSFCell c5 = r0.createCell(5);
        c5.setCellStyle(headerStyle);
        c5.setCellValue("入库时间");
        HSSFCell c6 = r0.createCell(6);
        c6.setCellStyle(headerStyle);
        c6.setCellValue("更新时间");
        for (int i = 0; i < list.size(); i++) {
            Asset asset = list.get(i);
            HSSFRow row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(asset.getId());
            row.createCell(1).setCellValue(asset.getStatusName());
            row.createCell(2).setCellValue(asset.getType());
            row.createCell(3).setCellValue(asset.getBrand());
            row.createCell(4).setCellValue(asset.getPrice());
            HSSFCell cell5 = row.createCell(5);
            cell5.setCellStyle(dateCellStyle);
            cell5.setCellValue(asset.getReadyDate());
            HSSFCell cell6 = row.createCell(6);
            cell6.setCellStyle(dateCellStyle);
            cell6.setCellValue(asset.getReadyDate());
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.setContentDispositionFormData("attachment", new String("资产信息表.xls".getBytes("UTF-8"), "ISO-8859-1"));
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            workbook.write(baos);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(baos.toByteArray(), headers, HttpStatus.CREATED);
    }

    /**
     * 订单记录转excel
     *
     * @param list
     * @return
     */
    public static ResponseEntity<byte[]> purchaseOrders2Excel(List<PurchaseOrder> list) {
        //1. 创建一个 Excel 文档
        HSSFWorkbook workbook = new HSSFWorkbook();
        //2. 创建文档摘要
        workbook.createInformationProperties();
        //3. 获取并配置文档信息
        DocumentSummaryInformation docInfo = workbook.getDocumentSummaryInformation();
        //文档类别
        docInfo.setCategory("订单信息");
        //文档管理员
        docInfo.setManager("admin");
        //设置公司信息
        docInfo.setCompany("bupt");
        //4. 获取文档摘要信息
        SummaryInformation summInfo = workbook.getSummaryInformation();
        //文档标题
        summInfo.setTitle("订单信息表");
        //文档作者
        summInfo.setAuthor("lams");
        // 文档备注
        summInfo.setComments("本文档由 lams 提供");
        //5. 创建样式
        //创建标题行的样式
        HSSFCellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.YELLOW.index);
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        HSSFCellStyle dateCellStyle = workbook.createCellStyle();
        dateCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy"));
        HSSFSheet sheet = workbook.createSheet("订单信息表");
        //设置列的宽度
        sheet.setColumnWidth(0, 10 * 256);
        sheet.setColumnWidth(1, 10 * 256);
        sheet.setColumnWidth(2, 13 * 256);
        sheet.setColumnWidth(3, 13 * 256);
        sheet.setColumnWidth(4, 13 * 256);
        sheet.setColumnWidth(5, 10 * 256);
        sheet.setColumnWidth(6, 10 * 256);
        sheet.setColumnWidth(8, 10 * 256);
        //6. 创建标题行
        HSSFRow r0 = sheet.createRow(0);
        HSSFCell c0 = r0.createCell(0);
        c0.setCellValue("订单号");
        c0.setCellStyle(headerStyle);
        HSSFCell c1 = r0.createCell(1);
        c1.setCellStyle(headerStyle);
        c1.setCellValue("订单名称");
        HSSFCell c2 = r0.createCell(2);
        c2.setCellStyle(headerStyle);
        c2.setCellValue("订单总价（元）");
        HSSFCell c3 = r0.createCell(3);
        c3.setCellStyle(headerStyle);
        c3.setCellValue("订单优惠（元）");
        HSSFCell c4 = r0.createCell(4);
        c4.setCellStyle(headerStyle);
        c4.setCellValue("实际支付（元）");
        HSSFCell c5 = r0.createCell(5);
        c5.setCellStyle(headerStyle);
        c5.setCellValue("是否有发票");
        HSSFCell c6 = r0.createCell(6);
        c6.setCellStyle(headerStyle);
        c6.setCellValue("订单备注");
        HSSFCell c7 = r0.createCell(7);
        c7.setCellStyle(headerStyle);
        c7.setCellValue("创建日期");
        HSSFCell c8 = r0.createCell(8);
        c8.setCellStyle(headerStyle);
        c8.setCellValue("更新日期");
        for (int i = 0; i < list.size(); i++) {
            PurchaseOrder purchaseOrder = list.get(i);
            HSSFRow row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(purchaseOrder.getId());
            row.createCell(1).setCellValue(purchaseOrder.getName());
            row.createCell(2).setCellValue(purchaseOrder.getTotal());
            row.createCell(3).setCellValue(purchaseOrder.getDiscount());
            row.createCell(4).setCellValue(purchaseOrder.getPay());
            row.createCell(5).setCellValue(purchaseOrder.getHasInvoice());
            row.createCell(6).setCellValue(purchaseOrder.getRemark());
            HSSFCell cell7 = row.createCell(7);
            cell7.setCellStyle(dateCellStyle);
            cell7.setCellValue(purchaseOrder.getCreateTime());
            HSSFCell cell8 = row.createCell(8);
            cell8.setCellStyle(dateCellStyle);
            cell8.setCellValue(purchaseOrder.getUpdateTime());
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.setContentDispositionFormData("attachment", new String("订单信息表.xls".getBytes("UTF-8"), "ISO-8859-1"));
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            workbook.write(baos);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(baos.toByteArray(), headers, HttpStatus.CREATED);
    }
}
