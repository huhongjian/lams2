package com.bupt.lams.utils;

import com.bupt.lams.constants.BudgetReimburseEnum;
import com.bupt.lams.constants.PlanItemTypeEnum;
import com.bupt.lams.dto.PlanItemData;
import com.bupt.lams.model.*;
import com.bupt.lams.model.planItem.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.hpsf.DocumentSummaryInformation;
import org.apache.poi.hpsf.SummaryInformation;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        sheet.setColumnWidth(2, 80 * 256);
        sheet.setColumnWidth(3, 10 * 256);
        sheet.setColumnWidth(4, 15 * 256);
        sheet.setColumnWidth(5, 10 * 256);
        sheet.setColumnWidth(6, 10 * 256);
        sheet.setColumnWidth(7, 15 * 256);
        sheet.setColumnWidth(8, 20 * 256);
        sheet.setColumnWidth(9, 15 * 256);
        sheet.setColumnWidth(10, 30 * 256);
        sheet.setColumnWidth(11, 10 * 256);
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
        c2.setCellValue("资产名称");
        HSSFCell c3 = r0.createCell(3);
        c3.setCellStyle(headerStyle);
        c3.setCellValue("类型");
        HSSFCell c4 = r0.createCell(4);
        c4.setCellStyle(headerStyle);
        c4.setCellValue("品牌");
        HSSFCell c5 = r0.createCell(5);
        c5.setCellStyle(headerStyle);
        c5.setCellValue("状态");
        HSSFCell c6 = r0.createCell(6);
        c6.setCellStyle(headerStyle);
        c6.setCellValue("价格（元）");
        HSSFCell c7 = r0.createCell(7);
        c7.setCellStyle(headerStyle);
        c7.setCellValue("申请人");
        HSSFCell c8 = r0.createCell(8);
        c8.setCellStyle(headerStyle);
        c8.setCellValue("申请人邮箱");
        HSSFCell c9 = r0.createCell(9);
        c9.setCellStyle(headerStyle);
        c9.setCellValue("申请人电话");
        HSSFCell c10 = r0.createCell(10);
        c10.setCellStyle(headerStyle);
        c10.setCellValue("申请理由");
        HSSFCell c11 = r0.createCell(11);
        c11.setCellStyle(headerStyle);
        c11.setCellValue("申请时间");
        int cnt = 0;
        for (int i = 0; i < list.size(); i++) {
            Order order = list.get(i);
            List<Asset> assetList = order.getAssetList();
            for (int j = 0; j < assetList.size(); j++) {
                Asset asset = assetList.get(j);
                HSSFRow row = sheet.createRow(++cnt);
                row.createCell(0).setCellValue(order.getId());
                row.createCell(1).setCellValue(asset.getId());
                row.createCell(2).setCellValue(asset.getAssetName());
                row.createCell(3).setCellValue(asset.getType());
                row.createCell(4).setCellValue(asset.getBrand());
                row.createCell(5).setCellValue(order.getStatusName());
                row.createCell(6).setCellValue(asset.getPrice());
                row.createCell(7).setCellValue(order.getUser().getName());
                row.createCell(8).setCellValue(order.getUserEmail());
                row.createCell(9).setCellValue(order.getUser().getPhone());
                row.createCell(10).setCellValue(order.getReason());
                HSSFCell cell11 = row.createCell(11);
                cell11.setCellStyle(dateCellStyle);
                cell11.setCellValue(order.getCreateTime());
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
        sheet.setColumnWidth(2, 80 * 256);
        sheet.setColumnWidth(3, 10 * 256);
        sheet.setColumnWidth(4, 15 * 256);
        sheet.setColumnWidth(5, 10 * 256);
        sheet.setColumnWidth(6, 15 * 256);
        sheet.setColumnWidth(7, 10 * 256);
        sheet.setColumnWidth(8, 20 * 256);
        sheet.setColumnWidth(9, 15 * 256);
        sheet.setColumnWidth(10, 30 * 256);
        sheet.setColumnWidth(11, 10 * 256);
        sheet.setColumnWidth(12, 10 * 256);
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
        c2.setCellValue("资产名称");
        HSSFCell c3 = r0.createCell(3);
        c3.setCellStyle(headerStyle);
        c3.setCellValue("类型");
        HSSFCell c4 = r0.createCell(4);
        c4.setCellStyle(headerStyle);
        c4.setCellValue("品牌");
        HSSFCell c5 = r0.createCell(5);
        c5.setCellStyle(headerStyle);
        c5.setCellValue("状态");
        HSSFCell c6 = r0.createCell(6);
        c6.setCellStyle(headerStyle);
        c6.setCellValue("预计归还时间");
        HSSFCell c7 = r0.createCell(7);
        c7.setCellStyle(headerStyle);
        c7.setCellValue("负责人");
        HSSFCell c8 = r0.createCell(8);
        c8.setCellStyle(headerStyle);
        c8.setCellValue("负责人邮箱");
        HSSFCell c9 = r0.createCell(9);
        c9.setCellStyle(headerStyle);
        c9.setCellValue("负责人电话");
        HSSFCell c10 = r0.createCell(10);
        c10.setCellStyle(headerStyle);
        c10.setCellValue("申请理由");
        HSSFCell c11 = r0.createCell(11);
        c11.setCellStyle(headerStyle);
        c11.setCellValue("申请时间");
        HSSFCell c12 = r0.createCell(12);
        c12.setCellStyle(headerStyle);
        c12.setCellValue("入库时间");
        int cnt = 0;
        for (int i = 0; i < list.size(); i++) {
            Order order = list.get(i);
            List<Asset> assetList = order.getAssetList();
            for (int j = 0; j < assetList.size(); j++) {
                Asset asset = assetList.get(j);
                HSSFRow row = sheet.createRow(++cnt);
                row.createCell(0).setCellValue(order.getId());
                row.createCell(1).setCellValue(asset.getId());
                row.createCell(2).setCellValue(asset.getAssetName());
                row.createCell(3).setCellValue(asset.getType());
                row.createCell(4).setCellValue(asset.getBrand());
                row.createCell(5).setCellValue(order.getStatusName());
                HSSFCell cell6 = row.createCell(6);
                cell6.setCellStyle(dateCellStyle);
                cell6.setCellValue(order.getExpireTime());
                row.createCell(7).setCellValue(order.getUser().getName());
                row.createCell(8).setCellValue(order.getUserEmail());
                row.createCell(9).setCellValue(order.getUser().getPhone());
                row.createCell(10).setCellValue(order.getReason());
                HSSFCell cell11 = row.createCell(11);
                cell11.setCellStyle(dateCellStyle);
                cell11.setCellValue(order.getCreateTime());
                HSSFCell cell12 = row.createCell(12);
                cell12.setCellStyle(dateCellStyle);
                cell12.setCellValue(asset.getReadyDate());
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
     * 归还记录转excel
     *
     * @param list
     * @return_
     */
    public static ResponseEntity<byte[]> assetReturn2Excel(List<Order> list) {
        //1. 创建一个 Excel 文档
        HSSFWorkbook workbook = new HSSFWorkbook();
        //2. 创建文档摘要
        workbook.createInformationProperties();
        //3. 获取并配置文档信息
        DocumentSummaryInformation docInfo = workbook.getDocumentSummaryInformation();
        //文档类别
        docInfo.setCategory("归还记录信息");
        //文档管理员
        docInfo.setManager("admin");
        //设置公司信息
        docInfo.setCompany("bupt");
        //4. 获取文档摘要信息
        SummaryInformation summInfo = workbook.getSummaryInformation();
        //文档标题
        summInfo.setTitle("归还记录信息表");
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
        HSSFSheet sheet = workbook.createSheet("归还记录信息表");
        //设置列的宽度
        sheet.setColumnWidth(0, 10 * 256);
        sheet.setColumnWidth(1, 10 * 256);
        sheet.setColumnWidth(2, 80 * 256);
        sheet.setColumnWidth(3, 10 * 256);
        sheet.setColumnWidth(4, 15 * 256);
        sheet.setColumnWidth(5, 10 * 256);
        sheet.setColumnWidth(6, 10 * 256);
        sheet.setColumnWidth(7, 20 * 256);
        sheet.setColumnWidth(8, 15 * 256);
        sheet.setColumnWidth(9, 10 * 256);
        //6. 创建标题行
        HSSFRow r0 = sheet.createRow(0);
        HSSFCell c0 = r0.createCell(0);
        c0.setCellValue("归还单号");
        c0.setCellStyle(headerStyle);
        HSSFCell c1 = r0.createCell(1);
        c1.setCellStyle(headerStyle);
        c1.setCellValue("资产编号");
        HSSFCell c2 = r0.createCell(2);
        c2.setCellStyle(headerStyle);
        c2.setCellValue("资产名称");
        HSSFCell c3 = r0.createCell(3);
        c3.setCellStyle(headerStyle);
        c3.setCellValue("类型");
        HSSFCell c4 = r0.createCell(4);
        c4.setCellStyle(headerStyle);
        c4.setCellValue("品牌");
        HSSFCell c5 = r0.createCell(5);
        c5.setCellStyle(headerStyle);
        c5.setCellValue("状态");
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
        c9.setCellValue("申请时间");
        int cnt = 0;
        for (int i = 0; i < list.size(); i++) {
            Order order = list.get(i);
            List<Asset> assetList = order.getAssetList();
            for (int j = 0; j < assetList.size(); j++) {
                Asset asset = assetList.get(j);
                HSSFRow row = sheet.createRow(++cnt);
                row.createCell(0).setCellValue(order.getId());
                row.createCell(1).setCellValue(asset.getId());
                row.createCell(2).setCellValue(asset.getAssetName());
                row.createCell(3).setCellValue(asset.getType());
                row.createCell(4).setCellValue(asset.getBrand());
                row.createCell(5).setCellValue(order.getStatusName());
                row.createCell(6).setCellValue(order.getUser().getName());
                row.createCell(7).setCellValue(order.getUserEmail());
                row.createCell(8).setCellValue(order.getUser().getPhone());
                HSSFCell cell9 = row.createCell(9);
                cell9.setCellStyle(dateCellStyle);
                cell9.setCellValue(order.getCreateTime());
            }
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.setContentDispositionFormData("attachment", new String("归还记录信息表.xls".getBytes("UTF-8"), "ISO-8859-1"));
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
        sheet.setColumnWidth(1, 80 * 256);
        sheet.setColumnWidth(2, 10 * 256);
        sheet.setColumnWidth(3, 10 * 256);
        sheet.setColumnWidth(4, 15 * 256);
        sheet.setColumnWidth(5, 10 * 256);
        sheet.setColumnWidth(6, 10 * 256);
        sheet.setColumnWidth(7, 10 * 256);
        //6. 创建标题行
        HSSFRow r0 = sheet.createRow(0);
        HSSFCell c0 = r0.createCell(0);
        c0.setCellValue("资产编号");
        c0.setCellStyle(headerStyle);
        HSSFCell c1 = r0.createCell(1);
        c1.setCellStyle(headerStyle);
        c1.setCellValue("资产名称");
        HSSFCell c2 = r0.createCell(2);
        c2.setCellStyle(headerStyle);
        c2.setCellValue("状态");
        HSSFCell c3 = r0.createCell(3);
        c3.setCellStyle(headerStyle);
        c3.setCellValue("类型");
        HSSFCell c4 = r0.createCell(4);
        c4.setCellStyle(headerStyle);
        c4.setCellValue("品牌");
        HSSFCell c5 = r0.createCell(5);
        c5.setCellStyle(headerStyle);
        c5.setCellValue("价格（元）");
        HSSFCell c6 = r0.createCell(6);
        c6.setCellStyle(headerStyle);
        c6.setCellValue("入库时间");
        HSSFCell c7 = r0.createCell(7);
        c7.setCellStyle(headerStyle);
        c7.setCellValue("更新时间");
        for (int i = 0; i < list.size(); i++) {
            Asset asset = list.get(i);
            HSSFRow row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(asset.getId());
            row.createCell(1).setCellValue(asset.getAssetName());
            row.createCell(2).setCellValue(asset.getStatusName());
            row.createCell(3).setCellValue(asset.getType());
            row.createCell(4).setCellValue(asset.getBrand());
            row.createCell(5).setCellValue(asset.getPrice());
            HSSFCell cell6 = row.createCell(6);
            cell6.setCellStyle(dateCellStyle);
            cell6.setCellValue(asset.getReadyDate());
            HSSFCell cell7 = row.createCell(7);
            cell7.setCellStyle(dateCellStyle);
            cell7.setCellValue(asset.getUpdateTime());
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
        sheet.setColumnWidth(1, 15 * 256);
        sheet.setColumnWidth(2, 10 * 256);
        sheet.setColumnWidth(3, 80 * 256);
        sheet.setColumnWidth(4, 10 * 256);
        sheet.setColumnWidth(5, 15 * 256);
        sheet.setColumnWidth(6, 10 * 256);
        sheet.setColumnWidth(7, 13 * 256);
        sheet.setColumnWidth(8, 13 * 256);
        sheet.setColumnWidth(9, 13 * 256);
        sheet.setColumnWidth(10, 10 * 256);
        sheet.setColumnWidth(11, 30 * 256);
        sheet.setColumnWidth(12, 15 * 256);
        sheet.setColumnWidth(13, 15 * 256);
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
        c2.setCellValue("资产编号");
        HSSFCell c3 = r0.createCell(3);
        c3.setCellStyle(headerStyle);
        c3.setCellValue("资产名称");
        HSSFCell c4 = r0.createCell(4);
        c4.setCellStyle(headerStyle);
        c4.setCellValue("类型");
        HSSFCell c5 = r0.createCell(5);
        c5.setCellStyle(headerStyle);
        c5.setCellValue("品牌");
        HSSFCell c6 = r0.createCell(6);
        c6.setCellStyle(headerStyle);
        c6.setCellValue("价格（元）");
        HSSFCell c7 = r0.createCell(7);
        c7.setCellStyle(headerStyle);
        c7.setCellValue("订单总价（元）");
        HSSFCell c8 = r0.createCell(8);
        c8.setCellStyle(headerStyle);
        c8.setCellValue("订单优惠（元）");
        HSSFCell c9 = r0.createCell(9);
        c9.setCellStyle(headerStyle);
        c9.setCellValue("实际支付（元）");
        HSSFCell c10 = r0.createCell(10);
        c10.setCellStyle(headerStyle);
        c10.setCellValue("是否有发票");
        HSSFCell c11 = r0.createCell(11);
        c11.setCellStyle(headerStyle);
        c11.setCellValue("订单备注");
        HSSFCell c12 = r0.createCell(12);
        c12.setCellStyle(headerStyle);
        c12.setCellValue("创建日期");
        HSSFCell c13 = r0.createCell(13);
        c13.setCellStyle(headerStyle);
        c13.setCellValue("更新日期");
        int cnt = 0;
        for (int i = 0; i < list.size(); i++) {
            PurchaseOrder purchaseOrder = list.get(i);
            List<Asset> assetList = purchaseOrder.getAssetList();
            for (int j = 0; j < assetList.size(); j++) {
                Asset asset = assetList.get(j);
                HSSFRow row = sheet.createRow(++cnt);
                row.createCell(0).setCellValue(purchaseOrder.getId());
                row.createCell(1).setCellValue(purchaseOrder.getName());
                row.createCell(2).setCellValue(asset.getId());
                row.createCell(3).setCellValue(asset.getAssetName());
                row.createCell(4).setCellValue(asset.getType());
                row.createCell(5).setCellValue(asset.getBrand());
                row.createCell(6).setCellValue(asset.getPrice());
                row.createCell(7).setCellValue(purchaseOrder.getTotal());
                row.createCell(8).setCellValue(purchaseOrder.getDiscount());
                row.createCell(9).setCellValue(purchaseOrder.getPay());
                row.createCell(10).setCellValue(purchaseOrder.getHasInvoice());
                row.createCell(11).setCellValue(purchaseOrder.getRemark());
                HSSFCell cell12 = row.createCell(12);
                cell12.setCellStyle(dateCellStyle);
                cell12.setCellValue(purchaseOrder.getCreateTime());
                HSSFCell cell13 = row.createCell(13);
                cell13.setCellStyle(dateCellStyle);
                cell13.setCellValue(purchaseOrder.getUpdateTime());
            }
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

    /**
     * 预算计划导入模板
     *
     * @return
     */
    public static ResponseEntity<byte[]> planExcelTemplate() {
        //1. 创建一个 Excel 文档
        HSSFWorkbook workbook = new HSSFWorkbook();
        //2. 创建文档摘要
        workbook.createInformationProperties();
        //3. 获取并配置文档信息
        DocumentSummaryInformation docInfo = workbook.getDocumentSummaryInformation();
        //文档类别
        docInfo.setCategory("预算计划");
        //文档管理员
        docInfo.setManager("admin");
        //设置公司信息
        docInfo.setCompany("bupt");
        //4. 获取文档摘要信息
        SummaryInformation summInfo = workbook.getSummaryInformation();
        //文档标题
        summInfo.setTitle("预算计划导入模板");
        //文档作者
        summInfo.setAuthor("lams");
        // 文档备注
        summInfo.setComments("本文档由 lams 提供");

        // 预算计划信息
        workbook = planTemplate(workbook);
        workbook = equipmentFeeTemplate(workbook);
        workbook = materialFeeTemplate(workbook);
        workbook = testingFeeTemplate(workbook);
        workbook = meetingFeeTemplate(workbook);
        workbook = researchFeeTemplate(workbook);
        workbook = academicTemplate(workbook);
        workbook = transFeeTemplate(workbook);
        workbook = publishFeeTemplate(workbook);
        workbook = paymentTemplate(workbook);
        workbook = advisoryTemplate(workbook);
        workbook = otherFeeTemplate(workbook);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.setContentDispositionFormData("attachment", new String("预算计划导入模板.xls".getBytes("UTF-8"), "ISO-8859-1"));
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            workbook.write(baos);
        } catch (
                IOException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(baos.toByteArray(), headers, HttpStatus.CREATED);
    }

    private static HSSFWorkbook planTemplate(HSSFWorkbook workbook) {
        HSSFCellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.YELLOW.index);
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        HSSFCellStyle dateCellStyle = workbook.createCellStyle();
        dateCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy"));
        HSSFSheet sheet = workbook.createSheet("预算计划");
        //设置列的宽度
        sheet.setColumnWidth(0, 50 * 256);
        sheet.setColumnWidth(1, 15 * 256);
        sheet.setColumnWidth(2, 50 * 256);
        sheet.setColumnWidth(3, 80 * 256);
        //6. 创建标题行
        HSSFRow r0 = sheet.createRow(0);
        HSSFCell c0 = r0.createCell(0);
        c0.setCellValue("预算计划名称");
        c0.setCellStyle(headerStyle);
        HSSFCell c1 = r0.createCell(1);
        c1.setCellStyle(headerStyle);
        c1.setCellValue("科研项目编号");
        HSSFCell c2 = r0.createCell(2);
        c2.setCellStyle(headerStyle);
        c2.setCellValue("科研项目名称");
        HSSFCell c3 = r0.createCell(3);
        c3.setCellStyle(headerStyle);
        c3.setCellValue("备注");

        return workbook;
    }

    private static HSSFWorkbook equipmentFeeTemplate(HSSFWorkbook workbook) {
        HSSFCellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.YELLOW.index);
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        HSSFCellStyle dateCellStyle = workbook.createCellStyle();
        dateCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy"));
        HSSFSheet sheet = workbook.createSheet("设备费");
        //设置列的宽度
        sheet.setColumnWidth(0, 50 * 256);
        sheet.setColumnWidth(1, 55 * 256);
        sheet.setColumnWidth(2, 10 * 256);
        sheet.setColumnWidth(3, 20 * 256);
        sheet.setColumnWidth(4, 20 * 256);
        sheet.setColumnWidth(5, 80 * 256);
        //6. 创建标题行
        HSSFRow r0 = sheet.createRow(0);
        HSSFCell c0 = r0.createCell(0);
        c0.setCellValue("设备名称");
        c0.setCellStyle(headerStyle);
        HSSFCell c1 = r0.createCell(1);
        c1.setCellStyle(headerStyle);
        c1.setCellValue("用途");
        HSSFCell c2 = r0.createCell(2);
        c2.setCellStyle(headerStyle);
        c2.setCellValue("数量");
        HSSFCell c3 = r0.createCell(3);
        c3.setCellStyle(headerStyle);
        c3.setCellValue("单价（万元）");
        HSSFCell c4 = r0.createCell(4);
        c4.setCellStyle(headerStyle);
        c4.setCellValue("合计（万元）");
        HSSFCell c5 = r0.createCell(5);
        c5.setCellStyle(headerStyle);
        c5.setCellValue("备注");

        return workbook;
    }

    private static HSSFWorkbook materialFeeTemplate(HSSFWorkbook workbook) {
        HSSFCellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.YELLOW.index);
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        HSSFCellStyle dateCellStyle = workbook.createCellStyle();
        dateCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy"));
        HSSFSheet sheet = workbook.createSheet("材料费");
        //设置列的宽度
        sheet.setColumnWidth(0, 50 * 256);
        sheet.setColumnWidth(1, 30 * 256);
        sheet.setColumnWidth(2, 50 * 256);
        sheet.setColumnWidth(3, 10 * 256);
        sheet.setColumnWidth(4, 20 * 256);
        sheet.setColumnWidth(5, 10 * 256);
        sheet.setColumnWidth(6, 20 * 256);
        sheet.setColumnWidth(7, 80 * 256);
        //6. 创建标题行
        HSSFRow r0 = sheet.createRow(0);
        HSSFCell c0 = r0.createCell(0);
        c0.setCellValue("材料名称");
        c0.setCellStyle(headerStyle);
        HSSFCell c1 = r0.createCell(1);
        c1.setCellStyle(headerStyle);
        c1.setCellValue("规格");
        HSSFCell c2 = r0.createCell(2);
        c2.setCellStyle(headerStyle);
        c2.setCellValue("用途");
        HSSFCell c3 = r0.createCell(3);
        c3.setCellStyle(headerStyle);
        c3.setCellValue("数量");
        HSSFCell c4 = r0.createCell(4);
        c4.setCellStyle(headerStyle);
        c4.setCellValue("单价（万元）");
        HSSFCell c5 = r0.createCell(5);
        c5.setCellStyle(headerStyle);
        c5.setCellValue("单位");
        HSSFCell c6 = r0.createCell(6);
        c6.setCellStyle(headerStyle);
        c6.setCellValue("合计（万元）");
        HSSFCell c7 = r0.createCell(7);
        c7.setCellStyle(headerStyle);
        c7.setCellValue("备注");

        return workbook;
    }

    private static HSSFWorkbook testingFeeTemplate(HSSFWorkbook workbook) {
        HSSFCellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.YELLOW.index);
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        HSSFCellStyle dateCellStyle = workbook.createCellStyle();
        dateCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy"));
        HSSFSheet sheet = workbook.createSheet("测试化验加工费");
        //设置列的宽度
        sheet.setColumnWidth(0, 50 * 256);
        sheet.setColumnWidth(1, 10 * 256);
        sheet.setColumnWidth(2, 20 * 256);
        sheet.setColumnWidth(3, 20 * 256);
        sheet.setColumnWidth(4, 80 * 256);
        //6. 创建标题行
        HSSFRow r0 = sheet.createRow(0);
        HSSFCell c0 = r0.createCell(0);
        c0.setCellValue("加工内容");
        c0.setCellStyle(headerStyle);
        HSSFCell c1 = r0.createCell(1);
        c1.setCellStyle(headerStyle);
        c1.setCellValue("次数");
        HSSFCell c2 = r0.createCell(2);
        c2.setCellStyle(headerStyle);
        c2.setCellValue("单价（万元）");
        HSSFCell c3 = r0.createCell(3);
        c3.setCellStyle(headerStyle);
        c3.setCellValue("合计（万元）");
        HSSFCell c4 = r0.createCell(4);
        c4.setCellStyle(headerStyle);
        c4.setCellValue("备注");

        return workbook;
    }

    private static HSSFWorkbook meetingFeeTemplate(HSSFWorkbook workbook) {
        HSSFCellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.YELLOW.index);
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        HSSFCellStyle dateCellStyle = workbook.createCellStyle();
        dateCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy"));
        HSSFSheet sheet = workbook.createSheet("会议费");
        //设置列的宽度
        sheet.setColumnWidth(0, 50 * 256);
        sheet.setColumnWidth(1, 50 * 256);
        sheet.setColumnWidth(2, 20 * 256);
        sheet.setColumnWidth(3, 20 * 256);
        sheet.setColumnWidth(4, 10 * 256);
        sheet.setColumnWidth(5, 10 * 256);
        sheet.setColumnWidth(6, 10 * 256);
        sheet.setColumnWidth(7, 10 * 256);
        sheet.setColumnWidth(8, 80 * 256);
        //6. 创建标题行
        HSSFRow r0 = sheet.createRow(0);
        HSSFCell c0 = r0.createCell(0);
        c0.setCellValue("会议内容");
        c0.setCellStyle(headerStyle);
        HSSFCell c1 = r0.createCell(1);
        c1.setCellStyle(headerStyle);
        c1.setCellValue("与课题相关性");
        HSSFCell c2 = r0.createCell(2);
        c2.setCellStyle(headerStyle);
        c2.setCellValue("伙食费（元/人/天）");
        HSSFCell c3 = r0.createCell(3);
        c3.setCellStyle(headerStyle);
        c3.setCellValue("其他费用（元/人/天）");
        HSSFCell c4 = r0.createCell(4);
        c4.setCellStyle(headerStyle);
        c4.setCellValue("天数");
        HSSFCell c5 = r0.createCell(5);
        c5.setCellStyle(headerStyle);
        c5.setCellValue("参会人数");
        HSSFCell c6 = r0.createCell(6);
        c6.setCellStyle(headerStyle);
        c6.setCellValue("会议次数");
        HSSFCell c7 = r0.createCell(7);
        c7.setCellStyle(headerStyle);
        c7.setCellValue("合计（万元）");
        HSSFCell c8 = r0.createCell(8);
        c8.setCellStyle(headerStyle);
        c8.setCellValue("备注");

        return workbook;
    }

    private static HSSFWorkbook researchFeeTemplate(HSSFWorkbook workbook) {
        HSSFCellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.YELLOW.index);
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        HSSFCellStyle dateCellStyle = workbook.createCellStyle();
        dateCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy"));
        HSSFSheet sheet = workbook.createSheet("课题调研差旅费");
        //设置列的宽度
        sheet.setColumnWidth(0, 50 * 256);
        sheet.setColumnWidth(1, 50 * 256);
        sheet.setColumnWidth(2, 20 * 256);
        sheet.setColumnWidth(3, 10 * 256);
        sheet.setColumnWidth(4, 20 * 256);
        sheet.setColumnWidth(5, 20 * 256);
        sheet.setColumnWidth(6, 20 * 256);
        sheet.setColumnWidth(7, 10 * 256);
        sheet.setColumnWidth(8, 20 * 256);
        sheet.setColumnWidth(9, 80 * 256);
        //6. 创建标题行
        HSSFRow r0 = sheet.createRow(0);
        HSSFCell c0 = r0.createCell(0);
        c0.setCellValue("调研地点");
        c0.setCellStyle(headerStyle);
        HSSFCell c1 = r0.createCell(1);
        c1.setCellStyle(headerStyle);
        c1.setCellValue("调研目的");
        HSSFCell c2 = r0.createCell(2);
        c2.setCellStyle(headerStyle);
        c2.setCellValue("人员类别");
        HSSFCell c3 = r0.createCell(3);
        c3.setCellStyle(headerStyle);
        c3.setCellValue("人次");
        HSSFCell c4 = r0.createCell(4);
        c4.setCellStyle(headerStyle);
        c4.setCellValue("往返交通费（元/人/天）");
        HSSFCell c5 = r0.createCell(5);
        c5.setCellStyle(headerStyle);
        c5.setCellValue("住宿（元/人/天）");
        HSSFCell c6 = r0.createCell(6);
        c6.setCellStyle(headerStyle);
        c6.setCellValue("公杂费（元/人/天）");
        HSSFCell c7 = r0.createCell(7);
        c7.setCellStyle(headerStyle);
        c7.setCellValue("天数");
        HSSFCell c8 = r0.createCell(8);
        c8.setCellStyle(headerStyle);
        c8.setCellValue("合计（万元）");
        HSSFCell c9 = r0.createCell(9);
        c9.setCellStyle(headerStyle);
        c9.setCellValue("备注");

        return workbook;
    }

    private static HSSFWorkbook academicTemplate(HSSFWorkbook workbook) {
        HSSFCellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.YELLOW.index);
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        HSSFCellStyle dateCellStyle = workbook.createCellStyle();
        dateCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy"));
        HSSFSheet sheet = workbook.createSheet("学术会议差旅费");
        //设置列的宽度
        sheet.setColumnWidth(0, 50 * 256);
        sheet.setColumnWidth(1, 20 * 256);
        sheet.setColumnWidth(2, 10 * 256);
        sheet.setColumnWidth(3, 20 * 256);
        sheet.setColumnWidth(4, 10 * 256);
        sheet.setColumnWidth(5, 20 * 256);
        sheet.setColumnWidth(6, 20 * 256);
        sheet.setColumnWidth(7, 20 * 256);
        sheet.setColumnWidth(8, 20 * 256);
        sheet.setColumnWidth(9, 80 * 256);
        //6. 创建标题行
        HSSFRow r0 = sheet.createRow(0);
        HSSFCell c0 = r0.createCell(0);
        c0.setCellValue("会议名称");
        c0.setCellStyle(headerStyle);
        HSSFCell c1 = r0.createCell(1);
        c1.setCellStyle(headerStyle);
        c1.setCellValue("人员类别");
        HSSFCell c2 = r0.createCell(2);
        c2.setCellStyle(headerStyle);
        c2.setCellValue("人次");
        HSSFCell c3 = r0.createCell(3);
        c3.setCellStyle(headerStyle);
        c3.setCellValue("年度");
        HSSFCell c4 = r0.createCell(4);
        c4.setCellStyle(headerStyle);
        c4.setCellValue("会期（天）");
        HSSFCell c5 = r0.createCell(5);
        c5.setCellStyle(headerStyle);
        c5.setCellValue("注册费（元/人）");
        HSSFCell c6 = r0.createCell(6);
        c6.setCellStyle(headerStyle);
        c6.setCellValue("往返交通费（元/人/天）");
        HSSFCell c7 = r0.createCell(7);
        c7.setCellStyle(headerStyle);
        c7.setCellValue("住宿及补助费（元/人/天）");
        HSSFCell c8 = r0.createCell(8);
        c8.setCellStyle(headerStyle);
        c8.setCellValue("合计（万元）");
        HSSFCell c9 = r0.createCell(9);
        c9.setCellStyle(headerStyle);
        c9.setCellValue("备注");

        return workbook;
    }

    private static HSSFWorkbook transFeeTemplate(HSSFWorkbook workbook) {
        HSSFCellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.YELLOW.index);
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        HSSFCellStyle dateCellStyle = workbook.createCellStyle();
        dateCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy"));
        HSSFSheet sheet = workbook.createSheet("北京市内交通费");
        //设置列的宽度
        sheet.setColumnWidth(0, 20 * 256);
        sheet.setColumnWidth(1, 80 * 256);
        //6. 创建标题行
        HSSFRow r0 = sheet.createRow(0);
        HSSFCell c0 = r0.createCell(0);
        c0.setCellStyle(headerStyle);
        c0.setCellValue("合计（万元）");
        HSSFCell c1 = r0.createCell(1);
        c1.setCellStyle(headerStyle);
        c1.setCellValue("备注");

        return workbook;
    }

    private static HSSFWorkbook publishFeeTemplate(HSSFWorkbook workbook) {
        HSSFCellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.YELLOW.index);
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        HSSFCellStyle dateCellStyle = workbook.createCellStyle();
        dateCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy"));
        HSSFSheet sheet = workbook.createSheet("出版、文献、信息传播、知识产权事务费");
        //设置列的宽度
        sheet.setColumnWidth(0, 50 * 256);
        sheet.setColumnWidth(1, 20 * 256);
        sheet.setColumnWidth(2, 80 * 256);
        //6. 创建标题行
        HSSFRow r0 = sheet.createRow(0);
        HSSFCell c0 = r0.createCell(0);
        c0.setCellValue("类别");
        c0.setCellStyle(headerStyle);
        HSSFCell c1 = r0.createCell(1);
        c1.setCellStyle(headerStyle);
        c1.setCellValue("合计（万元）");
        HSSFCell c2 = r0.createCell(2);
        c2.setCellStyle(headerStyle);
        c2.setCellValue("备注");

        return workbook;
    }

    private static HSSFWorkbook paymentTemplate(HSSFWorkbook workbook) {
        HSSFCellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.YELLOW.index);
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        HSSFCellStyle dateCellStyle = workbook.createCellStyle();
        dateCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy"));
        HSSFSheet sheet = workbook.createSheet("劳务费");
        //设置列的宽度
        sheet.setColumnWidth(0, 50 * 256);
        sheet.setColumnWidth(1, 50 * 256);
        sheet.setColumnWidth(2, 10 * 256);
        sheet.setColumnWidth(3, 20 * 256);
        sheet.setColumnWidth(4, 15 * 256);
        sheet.setColumnWidth(5, 20 * 256);
        sheet.setColumnWidth(6, 80 * 256);
        //6. 创建标题行
        HSSFRow r0 = sheet.createRow(0);
        HSSFCell c0 = r0.createCell(0);
        c0.setCellValue("类别");
        c0.setCellStyle(headerStyle);
        HSSFCell c1 = r0.createCell(1);
        c1.setCellStyle(headerStyle);
        c1.setCellValue("工作内容");
        HSSFCell c2 = r0.createCell(2);
        c2.setCellStyle(headerStyle);
        c2.setCellValue("数量");
        HSSFCell c3 = r0.createCell(3);
        c3.setCellStyle(headerStyle);
        c3.setCellValue("标准（元/人/月）");
        HSSFCell c4 = r0.createCell(4);
        c4.setCellStyle(headerStyle);
        c4.setCellValue("工作时间（月）");
        HSSFCell c5 = r0.createCell(5);
        c5.setCellStyle(headerStyle);
        c5.setCellValue("合计（万元）");
        HSSFCell c6 = r0.createCell(6);
        c6.setCellStyle(headerStyle);
        c6.setCellValue("备注");

        return workbook;
    }

    private static HSSFWorkbook advisoryTemplate(HSSFWorkbook workbook) {
        HSSFCellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.YELLOW.index);
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        HSSFCellStyle dateCellStyle = workbook.createCellStyle();
        dateCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy"));
        HSSFSheet sheet = workbook.createSheet("专家咨询费");
        //设置列的宽度
        sheet.setColumnWidth(0, 20 * 256);
        sheet.setColumnWidth(1, 50 * 256);
        sheet.setColumnWidth(2, 10 * 256);
        sheet.setColumnWidth(3, 20 * 256);
        sheet.setColumnWidth(4, 10 * 256);
        sheet.setColumnWidth(5, 20 * 256);
        sheet.setColumnWidth(6, 80 * 256);
        //6. 创建标题行
        HSSFRow r0 = sheet.createRow(0);
        HSSFCell c0 = r0.createCell(0);
        c0.setCellValue("人员种类");
        c0.setCellStyle(headerStyle);
        HSSFCell c1 = r0.createCell(1);
        c1.setCellStyle(headerStyle);
        c1.setCellValue("工作内容");
        HSSFCell c2 = r0.createCell(2);
        c2.setCellStyle(headerStyle);
        c2.setCellValue("人数");
        HSSFCell c3 = r0.createCell(3);
        c3.setCellStyle(headerStyle);
        c3.setCellValue("标准（元/人/天）");
        HSSFCell c4 = r0.createCell(4);
        c4.setCellStyle(headerStyle);
        c4.setCellValue("天数");
        HSSFCell c5 = r0.createCell(5);
        c5.setCellStyle(headerStyle);
        c5.setCellValue("合计（万元）");
        HSSFCell c6 = r0.createCell(6);
        c6.setCellStyle(headerStyle);
        c6.setCellValue("备注");

        return workbook;
    }

    private static HSSFWorkbook otherFeeTemplate(HSSFWorkbook workbook) {
        HSSFCellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.YELLOW.index);
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        HSSFCellStyle dateCellStyle = workbook.createCellStyle();
        dateCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy"));
        HSSFSheet sheet = workbook.createSheet("其他支出");
        //设置列的宽度
        sheet.setColumnWidth(0, 50 * 256);
        sheet.setColumnWidth(1, 20 * 256);
        sheet.setColumnWidth(2, 80 * 256);
        //6. 创建标题行
        HSSFRow r0 = sheet.createRow(0);
        HSSFCell c0 = r0.createCell(0);
        c0.setCellValue("类别");
        c0.setCellStyle(headerStyle);
        HSSFCell c1 = r0.createCell(1);
        c1.setCellStyle(headerStyle);
        c1.setCellValue("合计（万元）");
        HSSFCell c2 = r0.createCell(2);
        c2.setCellStyle(headerStyle);
        c2.setCellValue("备注");

        return workbook;
    }

    public static ResponseEntity<byte[]> plan2Excel(Plan plan) {
        //1. 创建一个 Excel 文档
        HSSFWorkbook workbook = new HSSFWorkbook();
        //2. 创建文档摘要
        workbook.createInformationProperties();
        //3. 获取并配置文档信息
        DocumentSummaryInformation docInfo = workbook.getDocumentSummaryInformation();
        //文档类别
        docInfo.setCategory("预算计划");
        //文档管理员
        docInfo.setManager("admin");
        //设置公司信息
        docInfo.setCompany("bupt");
        //4. 获取文档摘要信息
        SummaryInformation summInfo = workbook.getSummaryInformation();
        //文档标题
        summInfo.setTitle("预算计划表");
        //文档作者
        summInfo.setAuthor("lams");
        // 文档备注
        summInfo.setComments("本文档由 lams 提供");

        // 预算计划信息
        workbook = fillPlanInfo(plan, workbook);

        if (plan.getPlanItemDataMap() != null) {
            // 设备费信息
            if (plan.getPlanItemDataMap().get("equipmentFeeData") != null && CollectionUtils.isNotEmpty(plan.getPlanItemDataMap().get("equipmentFeeData").getPlanItemList())) {
                List<PlanItem> equipmentFeeList = plan.getPlanItemDataMap().get("equipmentFeeData").getPlanItemList();
                workbook = fillEquipmentFee((List) equipmentFeeList, workbook);
            }

            if (plan.getPlanItemDataMap().get("materialFeeData") != null && CollectionUtils.isNotEmpty(plan.getPlanItemDataMap().get("materialFeeData").getPlanItemList())) {
                List<PlanItem> materialFeeList = plan.getPlanItemDataMap().get("materialFeeData").getPlanItemList();
                workbook = fillMaterialFee((List) materialFeeList, workbook);
            }

            if (plan.getPlanItemDataMap().get("testingFeeData") != null && CollectionUtils.isNotEmpty(plan.getPlanItemDataMap().get("testingFeeData").getPlanItemList())) {
                List<PlanItem> testingFeeList = plan.getPlanItemDataMap().get("testingFeeData").getPlanItemList();
                workbook = fillTestingFee((List) testingFeeList, workbook);
            }

            if (plan.getPlanItemDataMap().get("meetingFeeData") != null && CollectionUtils.isNotEmpty(plan.getPlanItemDataMap().get("meetingFeeData").getPlanItemList())) {
                List<PlanItem> meetingFeeList = plan.getPlanItemDataMap().get("meetingFeeData").getPlanItemList();
                workbook = fillMeetingFee((List) meetingFeeList, workbook);
            }

            if (plan.getPlanItemDataMap().get("researchFeeData") != null && CollectionUtils.isNotEmpty(plan.getPlanItemDataMap().get("researchFeeData").getPlanItemList())) {
                List<PlanItem> researchFeeList = plan.getPlanItemDataMap().get("researchFeeData").getPlanItemList();
                workbook = fillResearchFee((List) researchFeeList, workbook);
            }

            if (plan.getPlanItemDataMap().get("academicFeeData") != null && CollectionUtils.isNotEmpty(plan.getPlanItemDataMap().get("academicFeeData").getPlanItemList())) {
                List<PlanItem> academicFeeList = plan.getPlanItemDataMap().get("academicFeeData").getPlanItemList();
                workbook = fillAcademicFee((List) academicFeeList, workbook);
            }

            if (plan.getPlanItemDataMap().get("transFeeData") != null && CollectionUtils.isNotEmpty(plan.getPlanItemDataMap().get("transFeeData").getPlanItemList())) {
                List<PlanItem> transFeeList = plan.getPlanItemDataMap().get("transFeeData").getPlanItemList();
                workbook = fillTransFee(transFeeList, workbook);
            }

            if (plan.getPlanItemDataMap().get("publishFeeData") != null && CollectionUtils.isNotEmpty(plan.getPlanItemDataMap().get("publishFeeData").getPlanItemList())) {
                List<PlanItem> publishFeeList = plan.getPlanItemDataMap().get("publishFeeData").getPlanItemList();
                workbook = fillPublishFee((List) publishFeeList, workbook);
            }

            if (plan.getPlanItemDataMap().get("paymentData") != null && CollectionUtils.isNotEmpty(plan.getPlanItemDataMap().get("paymentData").getPlanItemList())) {
                List<PlanItem> paymentList = plan.getPlanItemDataMap().get("paymentData").getPlanItemList();
                workbook = fillPayment((List) paymentList, workbook);
            }

            if (plan.getPlanItemDataMap().get("advisoryData") != null && CollectionUtils.isNotEmpty(plan.getPlanItemDataMap().get("advisoryData").getPlanItemList())) {
                List<PlanItem> advisoryList = plan.getPlanItemDataMap().get("advisoryData").getPlanItemList();
                workbook = fillAdvisory((List) advisoryList, workbook);
            }

            if (plan.getPlanItemDataMap().get("otherFeeData") != null && CollectionUtils.isNotEmpty(plan.getPlanItemDataMap().get("otherFeeData").getPlanItemList())) {
                List<PlanItem> otherFeeList = plan.getPlanItemDataMap().get("otherFeeData").getPlanItemList();
                workbook = fillOtherFee((List) otherFeeList, workbook);
            }
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.setContentDispositionFormData("attachment", new String("预算计划信息表.xls".getBytes("UTF-8"), "ISO-8859-1"));
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            workbook.write(baos);
        } catch (
                IOException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(baos.toByteArray(), headers, HttpStatus.CREATED);
    }

    public static ResponseEntity<byte[]> planStatics2Excel(Plan plan) {
        //1. 创建一个 Excel 文档
        HSSFWorkbook workbook = new HSSFWorkbook();
        //2. 创建文档摘要
        workbook.createInformationProperties();
        //3. 获取并配置文档信息
        DocumentSummaryInformation docInfo = workbook.getDocumentSummaryInformation();
        //文档类别
        docInfo.setCategory("经费使用情况");
        //文档管理员
        docInfo.setManager("admin");
        //设置公司信息
        docInfo.setCompany("bupt");
        //4. 获取文档摘要信息
        SummaryInformation summInfo = workbook.getSummaryInformation();
        //文档标题
        summInfo.setTitle("经费使用情况表");
        //文档作者
        summInfo.setAuthor("lams");
        // 文档备注
        summInfo.setComments("本文档由 lams 提供");

        HSSFCellStyle headerStyle = workbook.createCellStyle();
        HSSFCellStyle dateCellStyle = workbook.createCellStyle();
        dateCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy"));
        HSSFSheet sheet = workbook.createSheet("经费使用情况");
        //设置列的宽度
        sheet.setColumnWidth(0, 10 * 256);
        sheet.setColumnWidth(1, 35 * 256);
        sheet.setColumnWidth(2, 15 * 256);
        sheet.setColumnWidth(3, 15 * 256);
        sheet.setColumnWidth(4, 15 * 256);
        //6. 创建标题行
        HSSFRow r0 = sheet.createRow(0);
        HSSFCell c0 = r0.createCell(0);
        c0.setCellStyle(headerStyle);
        c0.setCellValue("序号");
        HSSFCell c1 = r0.createCell(1);
        c1.setCellStyle(headerStyle);
        c1.setCellValue("预算科目名称");
        HSSFCell c2 = r0.createCell(2);
        c2.setCellStyle(headerStyle);
        c2.setCellValue("金额（万元）");
        HSSFCell c3 = r0.createCell(3);
        c3.setCellStyle(headerStyle);
        c3.setCellValue("已使用");
        HSSFCell c4 = r0.createCell(4);
        c4.setCellStyle(headerStyle);
        c4.setCellValue("执行率%");
        int cnt = 0;

        HSSFRow row1 = sheet.createRow(++cnt);
        row1.createCell(0).setCellValue(cnt);
        row1.createCell(1).setCellValue("设备费");
        row1.createCell(2).setCellValue(0);
        row1.createCell(3).setCellValue(0);
        row1.createCell(4).setCellValue(0);
        if (plan.getPlanItemDataMap() != null && plan.getPlanItemDataMap().get("equipmentFeeData") != null) {
            PlanItemData data = plan.getPlanItemDataMap().get("equipmentFeeData");
            row1.createCell(2).setCellValue(data.getFunding());
            row1.createCell(3).setCellValue(data.getFunding() - data.getUnfulfilledQuota());
            row1.createCell(4).setCellValue(data.getExecuteRate());
        }

        HSSFRow row2 = sheet.createRow(++cnt);
        row2.createCell(0).setCellValue(cnt);
        row2.createCell(1).setCellValue("材料费");
        row2.createCell(2).setCellValue(0);
        row2.createCell(3).setCellValue(0);
        row2.createCell(4).setCellValue(0);
        if (plan.getPlanItemDataMap() != null && plan.getPlanItemDataMap().get("materialFeeData") != null) {
            PlanItemData data = plan.getPlanItemDataMap().get("materialFeeData");
            row2.createCell(2).setCellValue(data.getFunding());
            row2.createCell(3).setCellValue(data.getFunding() - data.getUnfulfilledQuota());
            row2.createCell(4).setCellValue(data.getExecuteRate());
        }

        HSSFRow row3 = sheet.createRow(++cnt);
        row3.createCell(0).setCellValue(cnt);
        row3.createCell(1).setCellValue("测试化验加工费");
        row3.createCell(2).setCellValue(0);
        row3.createCell(3).setCellValue(0);
        row3.createCell(4).setCellValue(0);
        if (plan.getPlanItemDataMap() != null && plan.getPlanItemDataMap().get("testingFeeData") != null) {
            PlanItemData data = plan.getPlanItemDataMap().get("testingFeeData");
            row3.createCell(2).setCellValue(data.getFunding());
            row3.createCell(3).setCellValue(data.getFunding() - data.getUnfulfilledQuota());
            row3.createCell(4).setCellValue(data.getExecuteRate());
        }

        HSSFRow row4 = sheet.createRow(++cnt);
        row4.createCell(0).setCellValue(cnt);
        row4.createCell(1).setCellValue("差旅/会议/国际合作与交流费");
        row4.createCell(2).setCellValue(0);
        row4.createCell(3).setCellValue(0);
        row4.createCell(4).setCellValue(0);
        Double total = 0D;
        Double unfulfilledQuota = 0D;
        if (plan.getPlanItemDataMap() != null && plan.getPlanItemDataMap().get("meetingFeeData") != null) {
            PlanItemData data = plan.getPlanItemDataMap().get("meetingFeeData");
            total += data.getFunding();
            unfulfilledQuota += data.getUnfulfilledQuota();
        }
        if (plan.getPlanItemDataMap() != null && plan.getPlanItemDataMap().get("researchFeeData") != null) {
            PlanItemData data = plan.getPlanItemDataMap().get("researchFeeData");
            total += data.getFunding();
            unfulfilledQuota += data.getUnfulfilledQuota();
        }
        if (plan.getPlanItemDataMap() != null && plan.getPlanItemDataMap().get("academicFeeData") != null) {
            PlanItemData data = plan.getPlanItemDataMap().get("academicFeeData");
            total += data.getFunding();
            unfulfilledQuota += data.getUnfulfilledQuota();
        }
        if (plan.getPlanItemDataMap() != null && plan.getPlanItemDataMap().get("transFeeData") != null) {
            PlanItemData data = plan.getPlanItemDataMap().get("transFeeData");
            total += data.getFunding();
            unfulfilledQuota += data.getUnfulfilledQuota();
        }
        row4.createCell(2).setCellValue(total);
        row4.createCell(3).setCellValue(unfulfilledQuota);
        row4.createCell(4).setCellValue(ComputeUtils.computeExecuteRate(total, unfulfilledQuota));

        HSSFRow row5 = sheet.createRow(++cnt);
        row5.createCell(0).setCellValue(cnt);
        row5.createCell(1).setCellValue("出版/文献/信息传播/知识产权事务费");
        row5.createCell(2).setCellValue(0);
        row5.createCell(3).setCellValue(0);
        row5.createCell(4).setCellValue(0);
        if (plan.getPlanItemDataMap() != null && plan.getPlanItemDataMap().get("publishFeeData") != null) {
            PlanItemData data = plan.getPlanItemDataMap().get("publishFeeData");
            row5.createCell(2).setCellValue(data.getFunding());
            row5.createCell(3).setCellValue(data.getFunding() - data.getUnfulfilledQuota());
            row5.createCell(4).setCellValue(data.getExecuteRate());
        }

        HSSFRow row6 = sheet.createRow(++cnt);
        row6.createCell(0).setCellValue(cnt);
        row6.createCell(1).setCellValue("劳务费");
        row6.createCell(2).setCellValue(0);
        row6.createCell(3).setCellValue(0);
        row6.createCell(4).setCellValue(0);
        if (plan.getPlanItemDataMap() != null && plan.getPlanItemDataMap().get("paymentData") != null) {
            PlanItemData data = plan.getPlanItemDataMap().get("paymentData");
            row6.createCell(2).setCellValue(data.getFunding());
            row6.createCell(3).setCellValue(data.getFunding() - data.getUnfulfilledQuota());
            row6.createCell(4).setCellValue(data.getExecuteRate());
        }

        HSSFRow row7 = sheet.createRow(++cnt);
        row7.createCell(0).setCellValue(cnt);
        row7.createCell(1).setCellValue("专家咨询费");
        row7.createCell(2).setCellValue(0);
        row7.createCell(3).setCellValue(0);
        row7.createCell(4).setCellValue(0);
        if (plan.getPlanItemDataMap() != null && plan.getPlanItemDataMap().get("advisoryData") != null) {
            PlanItemData data = plan.getPlanItemDataMap().get("advisoryData");
            row7.createCell(2).setCellValue(data.getFunding());
            row7.createCell(3).setCellValue(data.getFunding() - data.getUnfulfilledQuota());
            row7.createCell(4).setCellValue(data.getExecuteRate());
        }

        sheet.addMergedRegion(new CellRangeAddress(8, 8, 0, 1));
        HSSFRow row = sheet.createRow(++cnt);
        row.createCell(0).setCellValue("经费合计");
        row.createCell(2).setCellValue(0);
        row.createCell(3).setCellValue(0);
        row.createCell(4).setCellValue(0);
        row.createCell(2).setCellValue(plan.getFunding());
        row.createCell(3).setCellValue(plan.getFunding() - plan.getUnfulfilledQuota());
        row.createCell(4).setCellValue(plan.getExecuteRate());

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.setContentDispositionFormData("attachment", new String("经费使用情况表.xls".getBytes("UTF-8"), "ISO-8859-1"));
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            workbook.write(baos);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(baos.toByteArray(), headers, HttpStatus.CREATED);
    }

    private static HSSFWorkbook fillPlanInfo(Plan plan, HSSFWorkbook workbook) {
        HSSFCellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.YELLOW.index);
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        HSSFCellStyle dateCellStyle = workbook.createCellStyle();
        dateCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy"));
        HSSFSheet sheet = workbook.createSheet("预算计划");
        //设置列的宽度
        sheet.setColumnWidth(0, 15 * 256);
        sheet.setColumnWidth(1, 30 * 256);
        sheet.setColumnWidth(2, 15 * 256);
        sheet.setColumnWidth(3, 30 * 256);
        sheet.setColumnWidth(4, 15 * 256);
        sheet.setColumnWidth(5, 15 * 256);
        sheet.setColumnWidth(6, 10 * 256);
        sheet.setColumnWidth(7, 50 * 256);
        //6. 创建标题行
        HSSFRow r0 = sheet.createRow(0);
        HSSFCell c0 = r0.createCell(0);
        c0.setCellValue("预算计划编号");
        c0.setCellStyle(headerStyle);
        HSSFCell c1 = r0.createCell(1);
        c1.setCellStyle(headerStyle);
        c1.setCellValue("预算计划名称");
        HSSFCell c2 = r0.createCell(2);
        c2.setCellStyle(headerStyle);
        c2.setCellValue("科研项目编号");
        HSSFCell c3 = r0.createCell(3);
        c3.setCellStyle(headerStyle);
        c3.setCellValue("科研项目名称");
        HSSFCell c4 = r0.createCell(4);
        c4.setCellStyle(headerStyle);
        c4.setCellValue("资助额度（万元）");
        HSSFCell c5 = r0.createCell(5);
        c5.setCellStyle(headerStyle);
        c5.setCellValue("未执行额度（万元）");
        HSSFCell c6 = r0.createCell(6);
        c6.setCellStyle(headerStyle);
        c6.setCellValue("执行率（%）");
        HSSFCell c7 = r0.createCell(7);
        c7.setCellStyle(headerStyle);
        c7.setCellValue("备注");

        HSSFRow row = sheet.createRow(1);
        row.createCell(0).setCellValue(plan.getId());
        row.createCell(1).setCellValue(plan.getPlanName());
        row.createCell(2).setCellValue(plan.getProjectId());
        row.createCell(3).setCellValue(plan.getProjectName());
        row.createCell(4).setCellValue(plan.getFunding());
        row.createCell(5).setCellValue(plan.getUnfulfilledQuota());
        row.createCell(6).setCellValue(plan.getExecuteRate());
        row.createCell(7).setCellValue(plan.getRemark());

        return workbook;
    }

    private static HSSFWorkbook fillEquipmentFee(List<EquipmentFee> list, HSSFWorkbook workbook) {
        HSSFCellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.YELLOW.index);
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        HSSFCellStyle dateCellStyle = workbook.createCellStyle();
        dateCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy"));
        HSSFSheet sheet = workbook.createSheet("设备费");
        //设置列的宽度
        sheet.setColumnWidth(0, 10 * 256);
        sheet.setColumnWidth(1, 30 * 256);
        sheet.setColumnWidth(2, 30 * 256);
        sheet.setColumnWidth(3, 10 * 256);
        sheet.setColumnWidth(4, 10 * 256);
        sheet.setColumnWidth(5, 10 * 256);
        sheet.setColumnWidth(6, 15 * 256);
        sheet.setColumnWidth(7, 10 * 256);
        sheet.setColumnWidth(8, 50 * 256);
        //6. 创建标题行
        HSSFRow r0 = sheet.createRow(0);
        HSSFCell c0 = r0.createCell(0);
        c0.setCellStyle(headerStyle);
        c0.setCellValue("编号");
        HSSFCell c1 = r0.createCell(1);
        c1.setCellStyle(headerStyle);
        c1.setCellValue("设备名称");
        HSSFCell c2 = r0.createCell(2);
        c2.setCellStyle(headerStyle);
        c2.setCellValue("用途");
        HSSFCell c3 = r0.createCell(3);
        c3.setCellStyle(headerStyle);
        c3.setCellValue("数量");
        HSSFCell c4 = r0.createCell(4);
        c4.setCellStyle(headerStyle);
        c4.setCellValue("单价");
        HSSFCell c5 = r0.createCell(5);
        c5.setCellStyle(headerStyle);
        c5.setCellValue("合计（万元）");
        HSSFCell c6 = r0.createCell(6);
        c6.setCellStyle(headerStyle);
        c6.setCellValue("未执行额度（万元）");
        HSSFCell c7 = r0.createCell(7);
        c7.setCellStyle(headerStyle);
        c7.setCellValue("执行率（%）");
        HSSFCell c8 = r0.createCell(8);
        c8.setCellStyle(headerStyle);
        c8.setCellValue("备注");
        int cnt = 0;
        for (int i = 0; i < list.size(); i++) {
            EquipmentFee equipmentFee = list.get(i);
            HSSFRow row = sheet.createRow(++cnt);
            row.createCell(0).setCellValue(equipmentFee.getId());
            row.createCell(1).setCellValue(equipmentFee.getEquipmentName());
            row.createCell(2).setCellValue(equipmentFee.getUsage());
            row.createCell(3).setCellValue(equipmentFee.getAmount());
            row.createCell(4).setCellValue(equipmentFee.getUnitPrice());
            row.createCell(5).setCellValue(equipmentFee.getSubTotal());
            row.createCell(6).setCellValue(equipmentFee.getUnfulfilledQuota());
            row.createCell(7).setCellValue(equipmentFee.getExecuteRate());
            row.createCell(8).setCellValue(equipmentFee.getRemark());
        }
        return workbook;
    }

    private static HSSFWorkbook fillMaterialFee(List<MaterialFee> list, HSSFWorkbook workbook) {
        HSSFCellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.YELLOW.index);
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        HSSFCellStyle dateCellStyle = workbook.createCellStyle();
        dateCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy"));
        HSSFSheet sheet = workbook.createSheet("材料费");
        //设置列的宽度
        sheet.setColumnWidth(0, 10 * 256);
        sheet.setColumnWidth(1, 30 * 256);
        sheet.setColumnWidth(2, 30 * 256);
        sheet.setColumnWidth(3, 30 * 256);
        sheet.setColumnWidth(4, 10 * 256);
        sheet.setColumnWidth(5, 10 * 256);
        sheet.setColumnWidth(6, 10 * 256);
        sheet.setColumnWidth(7, 10 * 256);
        sheet.setColumnWidth(8, 10 * 256);
        sheet.setColumnWidth(9, 10 * 256);
        sheet.setColumnWidth(10, 50 * 256);
        //6. 创建标题行
        HSSFRow r0 = sheet.createRow(0);
        HSSFCell c0 = r0.createCell(0);
        c0.setCellStyle(headerStyle);
        c0.setCellValue("编号");
        HSSFCell c1 = r0.createCell(1);
        c1.setCellValue("材料名称");
        c1.setCellStyle(headerStyle);
        HSSFCell c2 = r0.createCell(2);
        c2.setCellStyle(headerStyle);
        c2.setCellValue("规格");
        HSSFCell c3 = r0.createCell(3);
        c3.setCellStyle(headerStyle);
        c3.setCellValue("用途");
        HSSFCell c4 = r0.createCell(4);
        c4.setCellStyle(headerStyle);
        c4.setCellValue("数量");
        HSSFCell c5 = r0.createCell(5);
        c5.setCellStyle(headerStyle);
        c5.setCellValue("单价（万元）");
        HSSFCell c6 = r0.createCell(6);
        c6.setCellStyle(headerStyle);
        c6.setCellValue("单位");
        HSSFCell c7 = r0.createCell(7);
        c7.setCellStyle(headerStyle);
        c7.setCellValue("小计（万元）");
        HSSFCell c8 = r0.createCell(8);
        c8.setCellStyle(headerStyle);
        c8.setCellValue("未执行额度（万元）");
        HSSFCell c9 = r0.createCell(9);
        c9.setCellStyle(headerStyle);
        c9.setCellValue("执行率（%）");
        HSSFCell c10 = r0.createCell(10);
        c10.setCellStyle(headerStyle);
        c10.setCellValue("备注");

        int cnt = 0;
        for (int i = 0; i < list.size(); i++) {
            MaterialFee materialFee = list.get(i);
            HSSFRow row = sheet.createRow(++cnt);
            row.createCell(0).setCellValue(materialFee.getId());
            row.createCell(1).setCellValue(materialFee.getMaterialName());
            row.createCell(2).setCellValue(materialFee.getSpecification());
            row.createCell(3).setCellValue(materialFee.getUsage());
            row.createCell(4).setCellValue(materialFee.getAmount());
            row.createCell(5).setCellValue(materialFee.getUnitPrice());
            row.createCell(6).setCellValue(materialFee.getUnit());
            row.createCell(7).setCellValue(materialFee.getSubTotal());
            row.createCell(8).setCellValue(materialFee.getUnfulfilledQuota());
            row.createCell(9).setCellValue(materialFee.getExecuteRate());
            row.createCell(10).setCellValue(materialFee.getRemark());
        }
        return workbook;
    }

    private static HSSFWorkbook fillTestingFee(List<TestingFee> list, HSSFWorkbook workbook) {
        HSSFCellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.YELLOW.index);
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        HSSFCellStyle dateCellStyle = workbook.createCellStyle();
        dateCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy"));
        HSSFSheet sheet = workbook.createSheet("测试化验加工费");
        //设置列的宽度
        sheet.setColumnWidth(0, 10 * 256);
        sheet.setColumnWidth(1, 30 * 256);
        sheet.setColumnWidth(2, 10 * 256);
        sheet.setColumnWidth(3, 10 * 256);
        sheet.setColumnWidth(4, 10 * 256);
        sheet.setColumnWidth(5, 15 * 256);
        sheet.setColumnWidth(6, 10 * 256);
        sheet.setColumnWidth(7, 50 * 256);
        //6. 创建标题行
        HSSFRow r0 = sheet.createRow(0);
        HSSFCell c0 = r0.createCell(0);
        c0.setCellStyle(headerStyle);
        c0.setCellValue("编号");
        HSSFCell c1 = r0.createCell(1);
        c1.setCellValue("加工内容");
        c1.setCellStyle(headerStyle);
        HSSFCell c2 = r0.createCell(2);
        c2.setCellStyle(headerStyle);
        c2.setCellValue("次数");
        HSSFCell c3 = r0.createCell(3);
        c3.setCellStyle(headerStyle);
        c3.setCellValue("单价（万元）");
        HSSFCell c4 = r0.createCell(4);
        c4.setCellStyle(headerStyle);
        c4.setCellValue("合计（万元）");
        HSSFCell c5 = r0.createCell(5);
        c5.setCellStyle(headerStyle);
        c5.setCellValue("未执行额度（万元）");
        HSSFCell c6 = r0.createCell(6);
        c6.setCellStyle(headerStyle);
        c6.setCellValue("执行率（%）");
        HSSFCell c7 = r0.createCell(7);
        c7.setCellStyle(headerStyle);
        c7.setCellValue("备注");

        int cnt = 0;
        for (int i = 0; i < list.size(); i++) {
            TestingFee testingFee = list.get(i);
            HSSFRow row = sheet.createRow(++cnt);
            row.createCell(0).setCellValue(testingFee.getId());
            row.createCell(1).setCellValue(testingFee.getContent());
            row.createCell(2).setCellValue(testingFee.getTimes());
            row.createCell(3).setCellValue(testingFee.getUnitPrice());
            row.createCell(4).setCellValue(testingFee.getSubTotal());
            row.createCell(5).setCellValue(testingFee.getUnfulfilledQuota());
            row.createCell(6).setCellValue(testingFee.getExecuteRate());
            row.createCell(7).setCellValue(testingFee.getRemark());
        }
        return workbook;
    }

    private static HSSFWorkbook fillMeetingFee(List<MeetingFee> list, HSSFWorkbook workbook) {
        HSSFCellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.YELLOW.index);
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        HSSFCellStyle dateCellStyle = workbook.createCellStyle();
        dateCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy"));
        HSSFSheet sheet = workbook.createSheet("会议费");
        //设置列的宽度
        sheet.setColumnWidth(0, 10 * 256);
        sheet.setColumnWidth(1, 30 * 256);
        sheet.setColumnWidth(2, 30 * 256);
        sheet.setColumnWidth(3, 15 * 256);
        sheet.setColumnWidth(4, 15 * 256);
        sheet.setColumnWidth(5, 10 * 256);
        sheet.setColumnWidth(6, 10 * 256);
        sheet.setColumnWidth(7, 10 * 256);
        sheet.setColumnWidth(8, 10 * 256);
        sheet.setColumnWidth(9, 15 * 256);
        sheet.setColumnWidth(10, 10 * 256);
        sheet.setColumnWidth(11, 50 * 256);
        //6. 创建标题行
        HSSFRow r0 = sheet.createRow(0);
        HSSFCell c0 = r0.createCell(0);
        c0.setCellStyle(headerStyle);
        c0.setCellValue("编号");
        HSSFCell c1 = r0.createCell(1);
        c1.setCellStyle(headerStyle);
        c1.setCellValue("会议内容");
        HSSFCell c2 = r0.createCell(2);
        c2.setCellStyle(headerStyle);
        c2.setCellValue("与课题相关性");
        HSSFCell c3 = r0.createCell(3);
        c3.setCellStyle(headerStyle);
        c3.setCellValue("伙食费（元/人/天）");
        HSSFCell c4 = r0.createCell(4);
        c4.setCellStyle(headerStyle);
        c4.setCellValue("其他费用（元/人/天）");
        HSSFCell c5 = r0.createCell(5);
        c5.setCellStyle(headerStyle);
        c5.setCellValue("天数");
        HSSFCell c6 = r0.createCell(6);
        c6.setCellStyle(headerStyle);
        c6.setCellValue("参会人数");
        HSSFCell c7 = r0.createCell(7);
        c7.setCellStyle(headerStyle);
        c7.setCellValue("会议次数");
        HSSFCell c8 = r0.createCell(8);
        c8.setCellStyle(headerStyle);
        c8.setCellValue("合计（万元）");
        HSSFCell c9 = r0.createCell(9);
        c9.setCellStyle(headerStyle);
        c9.setCellValue("未执行额度（万元）");
        HSSFCell c10 = r0.createCell(10);
        c10.setCellStyle(headerStyle);
        c10.setCellValue("执行率（%）");
        HSSFCell c11 = r0.createCell(11);
        c11.setCellStyle(headerStyle);
        c11.setCellValue("备注");

        int cnt = 0;
        for (int i = 0; i < list.size(); i++) {
            MeetingFee meetingFee = list.get(i);
            HSSFRow row = sheet.createRow(++cnt);
            row.createCell(0).setCellValue(meetingFee.getId());
            row.createCell(1).setCellValue(meetingFee.getContent());
            row.createCell(2).setCellValue(meetingFee.getRelation());
            row.createCell(3).setCellValue(meetingFee.getMeal());
            row.createCell(4).setCellValue(meetingFee.getOtherCost());
            row.createCell(5).setCellValue(meetingFee.getDays());
            row.createCell(6).setCellValue(meetingFee.getAmount());
            row.createCell(7).setCellValue(meetingFee.getMeetingNum());
            row.createCell(8).setCellValue(meetingFee.getSubTotal());
            row.createCell(9).setCellValue(meetingFee.getUnfulfilledQuota());
            row.createCell(10).setCellValue(meetingFee.getExecuteRate());
            row.createCell(11).setCellValue(meetingFee.getRemark());
        }
        return workbook;
    }

    private static HSSFWorkbook fillResearchFee(List<ResearchFee> list, HSSFWorkbook workbook) {
        HSSFCellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.YELLOW.index);
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        HSSFCellStyle dateCellStyle = workbook.createCellStyle();
        dateCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy"));
        HSSFSheet sheet = workbook.createSheet("课题调研差旅费");
        //设置列的宽度
        sheet.setColumnWidth(0, 10 * 256);
        sheet.setColumnWidth(1, 30 * 256);
        sheet.setColumnWidth(2, 30 * 256);
        sheet.setColumnWidth(3, 20 * 256);
        sheet.setColumnWidth(4, 10 * 256);
        sheet.setColumnWidth(5, 20 * 256);
        sheet.setColumnWidth(6, 15 * 256);
        sheet.setColumnWidth(7, 15 * 256);
        sheet.setColumnWidth(8, 10 * 256);
        sheet.setColumnWidth(9, 15 * 256);
        sheet.setColumnWidth(10, 15 * 256);
        sheet.setColumnWidth(11, 10 * 256);
        sheet.setColumnWidth(12, 50 * 256);
        //6. 创建标题行
        HSSFRow r0 = sheet.createRow(0);
        HSSFCell c0 = r0.createCell(0);
        c0.setCellStyle(headerStyle);
        c0.setCellValue("编号");
        HSSFCell c1 = r0.createCell(1);
        c1.setCellValue("调研地点");
        c1.setCellStyle(headerStyle);
        HSSFCell c2 = r0.createCell(2);
        c2.setCellStyle(headerStyle);
        c2.setCellValue("调研目的");
        HSSFCell c3 = r0.createCell(3);
        c3.setCellStyle(headerStyle);
        c3.setCellValue("人员类别");
        HSSFCell c4 = r0.createCell(4);
        c4.setCellStyle(headerStyle);
        c4.setCellValue("人次");
        HSSFCell c5 = r0.createCell(5);
        c5.setCellStyle(headerStyle);
        c5.setCellValue("往返交通费（元/人/天）");
        HSSFCell c6 = r0.createCell(6);
        c6.setCellStyle(headerStyle);
        c6.setCellValue("住宿（元/人/天）");
        HSSFCell c7 = r0.createCell(7);
        c7.setCellStyle(headerStyle);
        c7.setCellValue("公杂费（元/人/天）");
        HSSFCell c8 = r0.createCell(8);
        c8.setCellStyle(headerStyle);
        c8.setCellValue("天数");
        HSSFCell c9 = r0.createCell(9);
        c9.setCellStyle(headerStyle);
        c9.setCellValue("合计（万元）");
        HSSFCell c10 = r0.createCell(10);
        c10.setCellStyle(headerStyle);
        c10.setCellValue("未执行额度（万元）");
        HSSFCell c11 = r0.createCell(11);
        c11.setCellStyle(headerStyle);
        c11.setCellValue("执行率（%）");
        HSSFCell c12 = r0.createCell(12);
        c12.setCellStyle(headerStyle);
        c12.setCellValue("备注");

        int cnt = 0;
        for (int i = 0; i < list.size(); i++) {
            ResearchFee researchFee = list.get(i);
            HSSFRow row = sheet.createRow(++cnt);
            row.createCell(0).setCellValue(researchFee.getId());
            row.createCell(1).setCellValue(researchFee.getPlace());
            row.createCell(2).setCellValue(researchFee.getPurpose());
            row.createCell(3).setCellValue(researchFee.getPersonnelType());
            row.createCell(4).setCellValue(researchFee.getTimes());
            row.createCell(5).setCellValue(researchFee.getTransFee());
            row.createCell(6).setCellValue(researchFee.getAccommodationFee());
            row.createCell(7).setCellValue(researchFee.getOtherFee());
            row.createCell(8).setCellValue(researchFee.getDays());
            row.createCell(9).setCellValue(researchFee.getSubTotal());
            row.createCell(10).setCellValue(researchFee.getUnfulfilledQuota());
            row.createCell(11).setCellValue(researchFee.getExecuteRate());
            row.createCell(12).setCellValue(researchFee.getRemark());
        }
        return workbook;
    }

    private static HSSFWorkbook fillAcademicFee(List<AcademicFee> list, HSSFWorkbook workbook) {
        HSSFCellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.YELLOW.index);
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        HSSFCellStyle dateCellStyle = workbook.createCellStyle();
        dateCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy"));
        HSSFSheet sheet = workbook.createSheet("学术会议差旅费");
        //设置列的宽度
        sheet.setColumnWidth(0, 10 * 256);
        sheet.setColumnWidth(1, 30 * 256);
        sheet.setColumnWidth(2, 20 * 256);
        sheet.setColumnWidth(3, 10 * 256);
        sheet.setColumnWidth(4, 10 * 256);
        sheet.setColumnWidth(5, 10 * 256);
        sheet.setColumnWidth(6, 15 * 256);
        sheet.setColumnWidth(7, 20 * 256);
        sheet.setColumnWidth(8, 20 * 256);
        sheet.setColumnWidth(9, 15 * 256);
        sheet.setColumnWidth(10, 15 * 256);
        sheet.setColumnWidth(11, 10 * 256);
        sheet.setColumnWidth(12, 50 * 256);
        //6. 创建标题行
        HSSFRow r0 = sheet.createRow(0);
        HSSFCell c0 = r0.createCell(0);
        c0.setCellStyle(headerStyle);
        c0.setCellValue("编号");
        HSSFCell c1 = r0.createCell(1);
        c1.setCellStyle(headerStyle);
        c1.setCellValue("会议名称");
        HSSFCell c2 = r0.createCell(2);
        c2.setCellStyle(headerStyle);
        c2.setCellValue("人员类别");
        HSSFCell c3 = r0.createCell(3);
        c3.setCellStyle(headerStyle);
        c3.setCellValue("人次");
        HSSFCell c4 = r0.createCell(4);
        c4.setCellStyle(headerStyle);
        c4.setCellValue("年度");
        HSSFCell c5 = r0.createCell(5);
        c5.setCellStyle(headerStyle);
        c5.setCellValue("会期（天）");
        HSSFCell c6 = r0.createCell(6);
        c6.setCellStyle(headerStyle);
        c6.setCellValue("注册费（元/人）");
        HSSFCell c7 = r0.createCell(7);
        c7.setCellStyle(headerStyle);
        c7.setCellValue("往返交通费（元/人/天）");
        HSSFCell c8 = r0.createCell(8);
        c8.setCellStyle(headerStyle);
        c8.setCellValue("住宿及补助费（元/人/天）");
        HSSFCell c9 = r0.createCell(9);
        c9.setCellStyle(headerStyle);
        c9.setCellValue("合计（万元）");
        HSSFCell c10 = r0.createCell(10);
        c10.setCellStyle(headerStyle);
        c10.setCellValue("未执行额度（万元）");
        HSSFCell c11 = r0.createCell(11);
        c11.setCellStyle(headerStyle);
        c11.setCellValue("执行率（%）");
        HSSFCell c12 = r0.createCell(12);
        c12.setCellStyle(headerStyle);
        c12.setCellValue("备注");

        int cnt = 0;
        for (int i = 0; i < list.size(); i++) {
            AcademicFee materialFee = list.get(i);
            HSSFRow row = sheet.createRow(++cnt);
            row.createCell(0).setCellValue(materialFee.getId());
            row.createCell(1).setCellValue(materialFee.getMeetingName());
            row.createCell(2).setCellValue(materialFee.getPersonnelType());
            row.createCell(3).setCellValue(materialFee.getTimes());
            row.createCell(4).setCellValue(materialFee.getYear());
            row.createCell(5).setCellValue(materialFee.getDays());
            row.createCell(6).setCellValue(materialFee.getRegistrationFee());
            row.createCell(7).setCellValue(materialFee.getTransFee());
            row.createCell(8).setCellValue(materialFee.getAccommodationFee());
            row.createCell(9).setCellValue(materialFee.getSubTotal());
            row.createCell(10).setCellValue(materialFee.getUnfulfilledQuota());
            row.createCell(11).setCellValue(materialFee.getExecuteRate());
            row.createCell(12).setCellValue(materialFee.getRemark());
        }
        return workbook;
    }

    private static HSSFWorkbook fillTransFee(List<PlanItem> list, HSSFWorkbook workbook) {
        HSSFCellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.YELLOW.index);
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        HSSFCellStyle dateCellStyle = workbook.createCellStyle();
        dateCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy"));
        HSSFSheet sheet = workbook.createSheet("北京市内交通费");
        //设置列的宽度
        sheet.setColumnWidth(0, 10 * 256);
        sheet.setColumnWidth(1, 20 * 256);
        sheet.setColumnWidth(2, 20 * 256);
        sheet.setColumnWidth(3, 10 * 256);
        sheet.setColumnWidth(4, 80 * 256);
        //6. 创建标题行
        HSSFRow r0 = sheet.createRow(0);
        HSSFCell c0 = r0.createCell(0);
        c0.setCellStyle(headerStyle);
        c0.setCellValue("编号");
        HSSFCell c1 = r0.createCell(1);
        c1.setCellStyle(headerStyle);
        c1.setCellValue("合计（万元）");
        HSSFCell c2 = r0.createCell(2);
        c2.setCellStyle(headerStyle);
        c2.setCellValue("未执行额度（万元）");
        HSSFCell c3 = r0.createCell(3);
        c3.setCellStyle(headerStyle);
        c3.setCellValue("执行率（%）");
        HSSFCell c4 = r0.createCell(4);
        c4.setCellStyle(headerStyle);
        c4.setCellValue("备注");

        int cnt = 0;
        for (int i = 0; i < list.size(); i++) {
            PlanItem transFee = list.get(i);
            HSSFRow row = sheet.createRow(++cnt);
            row.createCell(0).setCellValue(transFee.getId());
            row.createCell(1).setCellValue(transFee.getSubTotal());
            row.createCell(2).setCellValue(transFee.getUnfulfilledQuota());
            row.createCell(3).setCellValue(transFee.getExecuteRate());
            row.createCell(4).setCellValue(transFee.getRemark());
        }
        return workbook;
    }

    private static HSSFWorkbook fillPublishFee(List<PublishFee> list, HSSFWorkbook workbook) {
        HSSFCellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.YELLOW.index);
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        HSSFCellStyle dateCellStyle = workbook.createCellStyle();
        dateCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy"));
        HSSFSheet sheet = workbook.createSheet("出版、文献、信息传播、知识产权事务费");
        //设置列的宽度
        sheet.setColumnWidth(0, 10 * 256);
        sheet.setColumnWidth(1, 50 * 256);
        sheet.setColumnWidth(2, 20 * 256);
        sheet.setColumnWidth(3, 20 * 256);
        sheet.setColumnWidth(4, 10 * 256);
        sheet.setColumnWidth(5, 80 * 256);
        //6. 创建标题行
        HSSFRow r0 = sheet.createRow(0);
        HSSFCell c0 = r0.createCell(0);
        c0.setCellStyle(headerStyle);
        c0.setCellValue("编号");
        HSSFCell c1 = r0.createCell(1);
        c1.setCellStyle(headerStyle);
        c1.setCellValue("类别");
        HSSFCell c2 = r0.createCell(2);
        c2.setCellStyle(headerStyle);
        c2.setCellValue("合计（万元）");
        HSSFCell c3 = r0.createCell(3);
        c3.setCellStyle(headerStyle);
        c3.setCellValue("未执行额度（万元）");
        HSSFCell c4 = r0.createCell(4);
        c4.setCellStyle(headerStyle);
        c4.setCellValue("执行率（%）");
        HSSFCell c5 = r0.createCell(5);
        c5.setCellStyle(headerStyle);
        c5.setCellValue("备注");

        int cnt = 0;
        for (int i = 0; i < list.size(); i++) {
            PublishFee publishFee = list.get(i);
            HSSFRow row = sheet.createRow(++cnt);
            row.createCell(0).setCellValue(publishFee.getId());
            row.createCell(1).setCellValue(publishFee.getType());
            row.createCell(2).setCellValue(publishFee.getSubTotal());
            row.createCell(3).setCellValue(publishFee.getUnfulfilledQuota());
            row.createCell(4).setCellValue(publishFee.getExecuteRate());
            row.createCell(5).setCellValue(publishFee.getRemark());
        }
        return workbook;
    }

    private static HSSFWorkbook fillPayment(List<Payment> list, HSSFWorkbook workbook) {
        HSSFCellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.YELLOW.index);
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        HSSFCellStyle dateCellStyle = workbook.createCellStyle();
        dateCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy"));
        HSSFSheet sheet = workbook.createSheet("劳务费");
        //设置列的宽度
        sheet.setColumnWidth(0, 10 * 256);
        sheet.setColumnWidth(1, 50 * 256);
        sheet.setColumnWidth(2, 50 * 256);
        sheet.setColumnWidth(3, 10 * 256);
        sheet.setColumnWidth(4, 20 * 256);
        sheet.setColumnWidth(5, 15 * 256);
        sheet.setColumnWidth(6, 20 * 256);
        sheet.setColumnWidth(7, 20 * 256);
        sheet.setColumnWidth(8, 10 * 256);
        sheet.setColumnWidth(9, 80 * 256);
        //6. 创建标题行
        HSSFRow r0 = sheet.createRow(0);
        HSSFCell c0 = r0.createCell(0);
        c0.setCellStyle(headerStyle);
        c0.setCellValue("编号");
        HSSFCell c1 = r0.createCell(1);
        c1.setCellStyle(headerStyle);
        c1.setCellValue("类别");
        HSSFCell c2 = r0.createCell(2);
        c2.setCellStyle(headerStyle);
        c2.setCellValue("工作内容");
        HSSFCell c3 = r0.createCell(3);
        c3.setCellStyle(headerStyle);
        c3.setCellValue("数量");
        HSSFCell c4 = r0.createCell(4);
        c4.setCellStyle(headerStyle);
        c4.setCellValue("标准（元/人/月）");
        HSSFCell c5 = r0.createCell(5);
        c5.setCellStyle(headerStyle);
        c5.setCellValue("工作时间（月）");
        HSSFCell c6 = r0.createCell(6);
        c6.setCellStyle(headerStyle);
        c6.setCellValue("合计（万元）");
        HSSFCell c7 = r0.createCell(7);
        c7.setCellStyle(headerStyle);
        c7.setCellValue("未执行额度（万元）");
        HSSFCell c8 = r0.createCell(8);
        c8.setCellStyle(headerStyle);
        c8.setCellValue("执行率（%）");
        HSSFCell c9 = r0.createCell(9);
        c9.setCellStyle(headerStyle);
        c9.setCellValue("备注");

        int cnt = 0;
        for (int i = 0; i < list.size(); i++) {
            Payment payment = list.get(i);
            HSSFRow row = sheet.createRow(++cnt);
            row.createCell(0).setCellValue(payment.getId());
            row.createCell(1).setCellValue(payment.getPersonnelType());
            row.createCell(2).setCellValue(payment.getContent());
            row.createCell(3).setCellValue(payment.getAmount());
            row.createCell(4).setCellValue(payment.getStandard());
            row.createCell(5).setCellValue(payment.getHours());
            row.createCell(6).setCellValue(payment.getSubTotal());
            row.createCell(7).setCellValue(payment.getUnfulfilledQuota());
            row.createCell(8).setCellValue(payment.getExecuteRate());
            row.createCell(9).setCellValue(payment.getRemark());
        }
        return workbook;
    }

    private static HSSFWorkbook fillAdvisory(List<Advisory> list, HSSFWorkbook workbook) {
        HSSFCellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.YELLOW.index);
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        HSSFCellStyle dateCellStyle = workbook.createCellStyle();
        dateCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy"));
        HSSFSheet sheet = workbook.createSheet("专家咨询费");
        //设置列的宽度
        sheet.setColumnWidth(0, 10 * 256);
        sheet.setColumnWidth(1, 20 * 256);
        sheet.setColumnWidth(2, 50 * 256);
        sheet.setColumnWidth(3, 10 * 256);
        sheet.setColumnWidth(4, 20 * 256);
        sheet.setColumnWidth(5, 10 * 256);
        sheet.setColumnWidth(6, 20 * 256);
        sheet.setColumnWidth(7, 20 * 256);
        sheet.setColumnWidth(8, 10 * 256);
        sheet.setColumnWidth(9, 80 * 256);
        //6. 创建标题行
        HSSFRow r0 = sheet.createRow(0);
        HSSFCell c0 = r0.createCell(0);
        c0.setCellStyle(headerStyle);
        c0.setCellValue("编号");
        HSSFCell c1 = r0.createCell(1);
        c1.setCellStyle(headerStyle);
        c1.setCellValue("人员种类");
        HSSFCell c2 = r0.createCell(2);
        c2.setCellStyle(headerStyle);
        c2.setCellValue("工作内容");
        HSSFCell c3 = r0.createCell(3);
        c3.setCellStyle(headerStyle);
        c3.setCellValue("人数");
        HSSFCell c4 = r0.createCell(4);
        c4.setCellStyle(headerStyle);
        c4.setCellValue("标准（元/人/天）");
        HSSFCell c5 = r0.createCell(5);
        c5.setCellStyle(headerStyle);
        c5.setCellValue("天数");
        HSSFCell c6 = r0.createCell(6);
        c6.setCellStyle(headerStyle);
        c6.setCellValue("合计（万元）");
        HSSFCell c7 = r0.createCell(7);
        c7.setCellStyle(headerStyle);
        c7.setCellValue("未执行额度（万元）");
        HSSFCell c8 = r0.createCell(8);
        c8.setCellStyle(headerStyle);
        c8.setCellValue("执行率（%）");
        HSSFCell c9 = r0.createCell(9);
        c9.setCellStyle(headerStyle);
        c9.setCellValue("备注");

        int cnt = 0;
        for (int i = 0; i < list.size(); i++) {
            Advisory advisory = list.get(i);
            HSSFRow row = sheet.createRow(++cnt);
            row.createCell(0).setCellValue(advisory.getId());
            row.createCell(1).setCellValue(advisory.getPersonnelType());
            row.createCell(2).setCellValue(advisory.getContent());
            row.createCell(3).setCellValue(advisory.getAmount());
            row.createCell(4).setCellValue(advisory.getStandard());
            row.createCell(5).setCellValue(advisory.getDays());
            row.createCell(6).setCellValue(advisory.getSubTotal());
            row.createCell(7).setCellValue(advisory.getUnfulfilledQuota());
            row.createCell(8).setCellValue(advisory.getExecuteRate());
            row.createCell(9).setCellValue(advisory.getRemark());
        }
        return workbook;
    }

    private static HSSFWorkbook fillOtherFee(List<OtherFee> list, HSSFWorkbook workbook) {
        HSSFCellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.YELLOW.index);
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        HSSFCellStyle dateCellStyle = workbook.createCellStyle();
        dateCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy"));
        HSSFSheet sheet = workbook.createSheet("其他支出");
        //设置列的宽度
        sheet.setColumnWidth(0, 10 * 256);
        sheet.setColumnWidth(1, 50 * 256);
        sheet.setColumnWidth(2, 20 * 256);
        sheet.setColumnWidth(3, 20 * 256);
        sheet.setColumnWidth(4, 10 * 256);
        sheet.setColumnWidth(5, 80 * 256);
        //6. 创建标题行
        HSSFRow r0 = sheet.createRow(0);
        HSSFCell c0 = r0.createCell(0);
        c0.setCellStyle(headerStyle);
        c0.setCellValue("编号");
        HSSFCell c1 = r0.createCell(1);
        c1.setCellStyle(headerStyle);
        c1.setCellValue("类别");
        HSSFCell c2 = r0.createCell(2);
        c2.setCellStyle(headerStyle);
        c2.setCellValue("合计（万元）");
        HSSFCell c3 = r0.createCell(3);
        c3.setCellStyle(headerStyle);
        c3.setCellValue("未执行额度（万元）");
        HSSFCell c4 = r0.createCell(4);
        c4.setCellStyle(headerStyle);
        c4.setCellValue("执行率（%）");
        HSSFCell c5 = r0.createCell(5);
        c5.setCellStyle(headerStyle);
        c5.setCellValue("备注");

        int cnt = 0;
        for (int i = 0; i < list.size(); i++) {
            OtherFee otherFee = list.get(i);
            HSSFRow row = sheet.createRow(++cnt);
            row.createCell(0).setCellValue(otherFee.getId());
            row.createCell(1).setCellValue(otherFee.getType());
            row.createCell(2).setCellValue(otherFee.getSubTotal());
            row.createCell(3).setCellValue(otherFee.getUnfulfilledQuota());
            row.createCell(4).setCellValue(otherFee.getExecuteRate());
            row.createCell(5).setCellValue(otherFee.getRemark());
        }
        return workbook;
    }

    /**
     * excel转预算流水
     */
    public static List<BudgetRecord> excel2Record(MultipartFile file) {
        List<BudgetRecord> list = new ArrayList<>();
        BudgetRecord record = null;
        try {
            //1. 创建一个 workbook 对象
            HSSFWorkbook workbook = new HSSFWorkbook(file.getInputStream());
            //2. 获取 workbook 中表单的数量
            int numberOfSheets = workbook.getNumberOfSheets();
            for (int i = 0; i < numberOfSheets; i++) {
                //3. 获取表单
                HSSFSheet sheet = workbook.getSheetAt(i);
                //4. 获取表单中的行数
                int physicalNumberOfRows = sheet.getPhysicalNumberOfRows();
                for (int j = 0; j < physicalNumberOfRows; j++) {
                    //5. 跳过标题行
                    if (j == 0) {
                        continue;//跳过标题行
                    }
                    //6. 获取行
                    HSSFRow row = sheet.getRow(j);
                    if (row == null) {
                        continue;//防止数据中间有空行
                    }
                    //7. 获取列数
                    int physicalNumberOfCells = row.getPhysicalNumberOfCells();
                    record = new BudgetRecord();
                    for (int k = 0; k < physicalNumberOfCells; k++) {
                        HSSFCell cell = row.getCell(k);
                        if (cell == null) {
                            continue;
                        }
                        switch (cell.getCellType()) {
                            case STRING:
                                String cellValue = cell.getStringCellValue();
                                switch (k) {
                                    case 1:
                                        record.setProjectName(cellValue);
                                        break;
                                    case 3:
                                        record.setPlanName(cellValue);
                                        break;
                                    case 4:
                                        record.setItemTypeName(cellValue);
                                        break;
                                    case 7:
                                        record.setReimburseName(cellValue);
                                        break;
                                    case 8:
                                        record.setRemark(cellValue);
                                        break;
                                }
                                break;
                            default: {
                                switch (k) {
                                    case 0:
                                        record.setProjectId((long) cell.getNumericCellValue());
                                        break;
                                    case 2:
                                        record.setPlanId((long) cell.getNumericCellValue());
                                        break;
                                    case 5:
                                        record.setPlanItemId((long) cell.getNumericCellValue());
                                        break;
                                    case 6:
                                        record.setFunding(cell.getNumericCellValue());
                                        break;
                                    case 9:
                                        record.setExecuteDate(cell.getDateCellValue());
                                        break;
                                }
                            }
                            break;
                        }
                    }
                    record.setUserEmail(UserInfoUtils.getLoginedUser().getUsername());
                    record.setItemType(PlanItemTypeEnum.getIndexByName(record.getItemTypeName()));
                    record.setIsReimburse(BudgetReimburseEnum.getIndexByName(record.getReimburseName()));
                    list.add(record);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 执行流水转excel
     *
     * @param list
     * @return
     */
    public static ResponseEntity<byte[]> budgetRecord2Excel(List<BudgetRecord> list) {
        //1. 创建一个 Excel 文档
        HSSFWorkbook workbook = new HSSFWorkbook();
        //2. 创建文档摘要
        workbook.createInformationProperties();
        //3. 获取并配置文档信息
        DocumentSummaryInformation docInfo = workbook.getDocumentSummaryInformation();
        //文档类别
        docInfo.setCategory("执行流水");
        //文档管理员
        docInfo.setManager("admin");
        //设置公司信息
        docInfo.setCompany("bupt");
        //4. 获取文档摘要信息
        SummaryInformation summInfo = workbook.getSummaryInformation();
        //文档标题
        summInfo.setTitle("执行流水表");
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
        HSSFSheet sheet = workbook.createSheet("执行流水表");
        //设置列的宽度
        sheet.setColumnWidth(0, 10 * 256);
        sheet.setColumnWidth(1, 10 * 256);
        sheet.setColumnWidth(2, 10 * 256);
        sheet.setColumnWidth(3, 50 * 256);
        sheet.setColumnWidth(4, 50 * 256);
        sheet.setColumnWidth(5, 30 * 256);
        sheet.setColumnWidth(6, 10 * 256);
        sheet.setColumnWidth(7, 50 * 256);
        sheet.setColumnWidth(8, 15 * 256);
        sheet.setColumnWidth(9, 15 * 256);
        //6. 创建标题行
        HSSFRow r0 = sheet.createRow(0);
        HSSFCell c0 = r0.createCell(0);
        c0.setCellValue("流水编号");
        c0.setCellStyle(headerStyle);
        HSSFCell c1 = r0.createCell(1);
        c1.setCellStyle(headerStyle);
        c1.setCellValue("是否报销");
        HSSFCell c2 = r0.createCell(2);
        c2.setCellStyle(headerStyle);
        c2.setCellValue("金额（万元）");
        HSSFCell c3 = r0.createCell(3);
        c3.setCellStyle(headerStyle);
        c3.setCellValue("科研项目");
        HSSFCell c4 = r0.createCell(4);
        c4.setCellStyle(headerStyle);
        c4.setCellValue("预算计划");
        HSSFCell c5 = r0.createCell(5);
        c5.setCellStyle(headerStyle);
        c5.setCellValue("预算科目");
        HSSFCell c6 = r0.createCell(6);
        c6.setCellStyle(headerStyle);
        c6.setCellValue("预算明细序号");
        HSSFCell c7 = r0.createCell(7);
        c7.setCellStyle(headerStyle);
        c7.setCellValue("备注");
        HSSFCell c8 = r0.createCell(8);
        c8.setCellStyle(headerStyle);
        c8.setCellValue("执行用户");
        HSSFCell c9 = r0.createCell(9);
        c9.setCellStyle(headerStyle);
        c9.setCellValue("执行时间");
        for (int i = 0; i < list.size(); i++) {
            BudgetRecord record = list.get(i);
            HSSFRow row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(record.getId());
            row.createCell(1).setCellValue(record.getReimburseName());
            row.createCell(2).setCellValue(record.getFunding());
            row.createCell(3).setCellValue(record.getProjectName());
            row.createCell(4).setCellValue(record.getPlanName());
            row.createCell(5).setCellValue(record.getItemTypeName());
            row.createCell(6).setCellValue(record.getPlanItemId());
            row.createCell(7).setCellValue(record.getRemark());
            row.createCell(8).setCellValue(record.getUser().getName());
            HSSFCell cell6 = row.createCell(9);
            cell6.setCellStyle(dateCellStyle);
            cell6.setCellValue(record.getExecuteDate());
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.setContentDispositionFormData("attachment", new String("执行流水表.xls".getBytes("UTF-8"), "ISO-8859-1"));
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            workbook.write(baos);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(baos.toByteArray(), headers, HttpStatus.CREATED);
    }

    /**
     * 执行流水导入模板
     *
     * @return
     */
    public static ResponseEntity<byte[]> budgetRecordExcelTemplate() {
        //1. 创建一个 Excel 文档
        HSSFWorkbook workbook = new HSSFWorkbook();
        //2. 创建文档摘要
        workbook.createInformationProperties();
        //3. 获取并配置文档信息
        DocumentSummaryInformation docInfo = workbook.getDocumentSummaryInformation();
        //文档类别
        docInfo.setCategory("执行流水");
        //文档管理员
        docInfo.setManager("admin");
        //设置公司信息
        docInfo.setCompany("bupt");
        //4. 获取文档摘要信息
        SummaryInformation summInfo = workbook.getSummaryInformation();
        //文档标题
        summInfo.setTitle("执行流水导入模板");
        //文档作者
        summInfo.setAuthor("lams");
        // 文档备注
        summInfo.setComments("本文档由 lams 提供");

        // 执行流水信息
        HSSFCellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.YELLOW.index);
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        HSSFCellStyle dateCellStyle = workbook.createCellStyle();
        dateCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy"));
        HSSFSheet sheet = workbook.createSheet("执行流水");
        //设置列的宽度
        sheet.setColumnWidth(0, 15 * 256);
        sheet.setColumnWidth(1, 30 * 256);
        sheet.setColumnWidth(2, 15 * 256);
        sheet.setColumnWidth(3, 30 * 256);
        sheet.setColumnWidth(4, 15 * 256);
        sheet.setColumnWidth(5, 15 * 256);
        sheet.setColumnWidth(6, 15 * 256);
        sheet.setColumnWidth(7, 15 * 256);
        sheet.setColumnWidth(8, 50 * 256);
        sheet.setColumnWidth(9, 15 * 256);

        //6. 创建标题行
        HSSFRow r0 = sheet.createRow(0);
        HSSFCell c0 = r0.createCell(0);
        c0.setCellValue("科研项目编号");
        c0.setCellStyle(headerStyle);
        HSSFCell c1 = r0.createCell(1);
        c1.setCellStyle(headerStyle);
        c1.setCellValue("科研项目名称");
        HSSFCell c2 = r0.createCell(2);
        c2.setCellStyle(headerStyle);
        c2.setCellValue("预算计划编号");
        HSSFCell c3 = r0.createCell(3);
        c3.setCellStyle(headerStyle);
        c3.setCellValue("预算计划名称");
        HSSFCell c4 = r0.createCell(4);
        c4.setCellStyle(headerStyle);
        c4.setCellValue("预算科目（请严格按照系统命名，不能有换行）");
        HSSFCell c5 = r0.createCell(5);
        c5.setCellStyle(headerStyle);
        c5.setCellValue("预算明细编号");
        HSSFCell c6 = r0.createCell(6);
        c6.setCellStyle(headerStyle);
        c6.setCellValue("金额（万元）");
        HSSFCell c7 = r0.createCell(7);
        c7.setCellStyle(headerStyle);
        c7.setCellValue("已报销/未报销");
        HSSFCell c8 = r0.createCell(8);
        c8.setCellStyle(headerStyle);
        c8.setCellValue("备注");
        HSSFCell c9 = r0.createCell(9);
        c9.setCellStyle(headerStyle);
        c9.setCellValue("执行时间");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.setContentDispositionFormData("attachment", new String("执行流水导入模板.xls".getBytes("UTF-8"), "ISO-8859-1"));
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            workbook.write(baos);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(baos.toByteArray(), headers, HttpStatus.CREATED);
    }


}
