package com.kuangji.paopao.controller;

import com.kuangji.paopao.constant.ExcelConstant;
import com.kuangji.paopao.dto.result.ExcelCheckResult;
import com.kuangji.paopao.service.ArticleService;
import com.kuangji.paopao.util.ServiceResultUtils;
import com.kuangji.paopao.util.excel.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.List;

@RestController
@RequestMapping("/static/excel")
public class ExcelController {
    @Autowired
    private ArticleService articleService;

    /**
     * <p>
     * 批量导入用户(直接导入)
     */

    @PostMapping("/upload/article")
    public Object uploadStation(@RequestParam(value = "userId", required = false) Integer userId, @RequestParam("file") MultipartFile multipartfile) {
        File file = FileUtil.convertMultipartFileToFile(multipartfile);
        if (file == null) {
            return ServiceResultUtils.failure("导入失败！");
        }
        List<ExcelCheckResult> list = articleService.checkArticleExcel(file, userId);
        if (list.size() > 0) {
            return ServiceResultUtils.failure(list);
        } else {
            int result = articleService.batchAddArticleByExcel(file, userId);
            return ServiceResultUtils.success(result>0);
        }

    }

    @RequestMapping("/download/{id}")
    public void downloadExcel(@PathVariable("id") Integer id, HttpServletResponse res) {
        try {

            //获取要下载的模板名称
            String fileName;
            switch (id) {
                case 1:
                    fileName = ExcelConstant.ARTICLE_TEMPLATE;
                    break;
                default:
                    fileName = ExcelConstant.ARTICLE_TEMPLATE;
                    break;
            }
            //设置要下载的文件的名称
            res.setHeader("Content-disposition", "attachment;fileName=" + fileName);
            //通知客服文件的MIME类型
            res.setContentType("application/vnd.ms-excel;charset=UTF-8");
            //获取文件的路径
            //String filePath = getClass().getResource("/template/" + fileName).getPath();
            //excel模板路径
            File cfgFile = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + "static/excel/" + fileName);
            FileInputStream input = new FileInputStream(cfgFile);
            //            FileInputStream input = new FileInputStream(new File("d://"+fileName));
            OutputStream out = res.getOutputStream();
            byte[] b = new byte[2048];
            int len;
            while ((len = input.read(b)) != -1) {
                out.write(b, 0, len);
            }
            //修正 Excel在“xxx.xlsx”中发现不可读取的内容。是否恢复此工作薄的内容？如果信任此工作簿的来源，请点击"是"
            res.setHeader("Content-Length", String.valueOf(input.getChannel().size()));
            input.close();
            out.close();
            System.out.println("应用导入模板下载完成");
        } catch (Exception ex) {
            System.out.println("应用导入模板下载失败！" + ex.getMessage());
        }
    }
}

