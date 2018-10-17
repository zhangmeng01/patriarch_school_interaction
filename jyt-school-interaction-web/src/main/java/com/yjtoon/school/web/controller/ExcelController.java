package com.yjtoon.school.web.controller;

import java.io.*;
import java.lang.Boolean;
import java.math.BigDecimal;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

//import com.jyt.xiaotoon.admin.service.CourseTeacherRelService;
//import com.jyt.xiaotoon.admin.service.entity.CourseTeacherRelBean;
import com.jyt.user.service.entity.ClassInfo;
import com.jyt.user.service.entity.ClassTeacherRel;
import com.jyt.user.service.entity.Grade;
import com.yjtoon.school.util.Const;
import com.yjtoon.school.web.utils.URLencodeUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.validator.internal.util.privilegedactions.GetResource;
import org.jsoup.helper.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.alibaba.dubbo.config.annotation.Reference;
import com.jyt.user.service.IOrganizationService;
import com.jyt.user.service.IUserService;
import com.jyt.user.service.entity.UserStudent;
import com.jyt.user.service.response.CommonResponse;
//import com.jyt.xiaotoon.admin.service.CourseTeacherRelService;
//import com.jyt.xiaotoon.admin.service.entity.CourseTeacherRelBean;
//import com.jyt.xiaotoon.core.entity.CourseTeacherRel;
import com.yjtoon.framework.annotations.LogWrapper;
import com.yjtoon.framework.annotations.ReturnWrapper;
import com.yjtoon.framework.exception.ReturnInfo;
import com.yjtoon.school.IAchievementService;
import com.yjtoon.school.IExamService;
import com.yjtoon.school.IExamSubjectService;
import com.yjtoon.school.domain.Achievement;
import com.yjtoon.school.domain.Exam;
import com.yjtoon.school.domain.ExamSubject;
import com.yjtoon.school.dto.ExcelImportDto;
import com.yjtoon.school.dto.ExcelWarnDto;
import com.yjtoon.school.enums.MessageCode;
import com.yjtoon.school.util.Constants;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import jxl.Workbook;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.write.*;

@Controller
@RequestMapping("/jxhd/excel")
@LogWrapper
@ReturnWrapper
@Validated
@RestController
@Api(value = "/jxhd/excel", description = "excel导入导出")
public class ExcelController extends BaseController {
	
    private static Logger logger = LoggerFactory.getLogger(ExcelController.class);
    @Autowired
    private IExamService iExamService;
    @Autowired
    private IExamSubjectService iExamSubjectService;
    @Autowired
    private IAchievementService iAchievementService;

    @Reference(version="1.0.0")
    private IUserService iUserService;
    @Reference(version="1.0.0")
    private IOrganizationService iOrganizationService;
    @RequestMapping(value = "/exportExamTmp")
    @ApiOperation(value = "下载成绩模板", httpMethod = "GET", notes = "下载成绩模板")
    public void exportExamTmp(HttpServletRequest request, HttpServletResponse response) {
        try {
            String userAgent = request.getHeader("User-Agent");
            String fileName = URLencodeUtil.encode("成绩导入模板.xlsx", userAgent);
            response.setCharacterEncoding("utf-8");
            response.setContentType("multipart/form-data");
            //处理下载弹出框名字的编码问题
            response.setHeader("Content-Disposition", "attachment;fileName="
                    + new String( fileName.getBytes("utf-8"), "ISO8859-1" ));
            //获取文件的下载路径
            //利用输入输出流对文件进行下载
            InputStream inputStream=request.getServletContext().getResourceAsStream( "/WEB-INF/classes/html/excel/file.xlsx" );
            OutputStream os = response.getOutputStream();
            byte[] b = new byte[2048];
            int length;
            while ((length = inputStream.read(b)) > 0) {
                os.write(b, 0, length);
            }
            // 这里主要关闭。
            os.close();
            inputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "importExamTmp", method = RequestMethod.POST)
    @ApiOperation(value = "导入成绩", httpMethod = "POST", notes = "导入成绩")
    public ReturnInfo<ExcelImportDto> importExamTmp(HttpServletRequest request, HttpServletResponse response,
            @ApiParam(name = "file",  value = "文件", required = true) @RequestParam(value="file") MultipartFile file,
            @ApiParam(name = "vaYearNum",  value = "学期", required = true) @RequestParam String vaYearNum,
            @ApiParam(name = "classIfy",  value = "考试分类", required = true) @RequestParam String classIfy,
            @ApiParam(name = "exName",  value = "考试名称", required = true) @RequestParam String exName) {
        ReturnInfo<ExcelImportDto> rs = new ReturnInfo<ExcelImportDto>();
        ExcelImportDto excelImportDto = new ExcelImportDto();
        Date date = new Date();
        Exam exam = new Exam();
        //在session中拿到登陆教师的id
        HttpSession session = request.getSession();
        Long utId = (Long)session.getAttribute(Const.UT_ID);
        //根据教师ID查询年级，班级
        ClassTeacherRel classTeacherRel = new ClassTeacherRel();
        classTeacherRel.setCtrIsDelete(Constants.IS_DELETE_N);
        classTeacherRel.setUtId(utId);
        classTeacherRel.setCtrIsHeadmaster(Constants.IS_MASTER_TEACHER_Y);
        Long ciId = 0L;
        Long grId = 0L;
        String ciName = "";
        String grName = "";
        if(utId==null||utId==0L){
            excelImportDto.setCode(MessageCode.EXCEL_IMPORT_FAIL_UTID_IS_NULL.getCode());
            excelImportDto.setMessage(MessageCode.EXCEL_IMPORT_FAIL_UTID_IS_NULL.getMessage());
            excelImportDto.setExcelWarnDtoList(null);
            rs.setData(excelImportDto);
            return rs;
        }
        CommonResponse<List<ClassTeacherRel>> comClassTeacherRelList = iUserService.findClassTeacherRelList(classTeacherRel);
        if(comClassTeacherRelList.getData()!=null&&comClassTeacherRelList.getData().size()>0) {
            ciId = comClassTeacherRelList.getData().get(0).getCiId();
            CommonResponse<ClassInfo> comClassInfo = iOrganizationService.findClassInfoById(comClassTeacherRelList.getData().get(0).getCiId());
            CommonResponse<Grade> comGrade = iOrganizationService.findGradeById(comClassTeacherRelList.getData().get(0).getGrId());
            ciId = comClassInfo.getData().getCiId();
            ciName = comClassInfo.getData().getCiName();
            grId = comGrade.getData().getGrId();
            grName = comGrade.getData().getGrName();
        }else{
            excelImportDto.setCode(MessageCode.EXCEL_IMPORT_FAIL_CLASSINFO_IS_NULL.getCode());
            excelImportDto.setMessage(MessageCode.EXCEL_IMPORT_FAIL_CLASSINFO_IS_NULL.getMessage());
            excelImportDto.setExcelWarnDtoList(null);
            rs.setData(excelImportDto);
            return rs;
        }
        exam.setGrId(grId);
        exam.setGrName(grName);
        exam.setCiId(ciId);
        exam.setCiName(ciName);
        exam.setExIsDelete(Constants.IS_DELETE_N);
        exam.setVaYearNum(vaYearNum);
        exam.setExClassify(classIfy);
        exam.setCreateTime(date);
        exam.setUpdateTime(date);
        exam.setExName(exName);

        List<ExamSubject> examSubjectList = new ArrayList<ExamSubject>();
        List<Achievement> achievementList = new ArrayList<Achievement>();
        List<ExcelWarnDto> excelWarnDtoList = new ArrayList<ExcelWarnDto>();
        List<BigDecimal> totalScoreList = new ArrayList<>();
        List<String> usXjhList = new ArrayList<>();

        UserStudent userStudent = new UserStudent();
        userStudent.setCiId(ciId);
        userStudent.setUsIsDelete(Constants.IS_DELETE_N);
        CommonResponse<List<UserStudent>> comList = iUserService.findUserStudentList(userStudent);
        try{
            ArrayList<ArrayList<Object>> rowList = new ArrayList<ArrayList<Object>>();
            ArrayList<Object> colList;
//            HSSFWorkbook wb = new HSSFWorkbook(file.getInputStream());
//            HSSFSheet sheet = wb.getSheetAt(0);
//            HSSFRow row;
//            HSSFCell cell;
            XSSFWorkbook wb = new XSSFWorkbook(file.getInputStream());
            XSSFSheet sheet = wb.getSheetAt(0);
            XSSFRow row;
            XSSFCell cell;
            Object value;
            List<String> excelSubjectList = new ArrayList<String>();
            int successRow = 0;
            boolean cell_flag = false;
            for(int i = sheet.getFirstRowNum() , rowCount = 0; rowCount < sheet.getPhysicalNumberOfRows() ; i++ ){
                if(cell_flag)break;
                row = sheet.getRow(i);
                colList = new ArrayList<Object>();
                if(i==0){
                    if(row == null){
                        //当读取行为空时
                        if(i != sheet.getPhysicalNumberOfRows()){//判断是否是最后一行
                            rowList.add(colList);
                        }
                        continue;
                    }else{
                        rowCount++;
                    }
                    //行的读取逻辑
                    for( int j = row.getFirstCellNum() ; j <= row.getLastCellNum() ;j++){
                        cell = row.getCell(j);
                        if(cell == null || cell.getCellType() == HSSFCell.CELL_TYPE_BLANK){
                            //当该单元格为空
                            if(j != row.getLastCellNum()){//判断是否是该行中最后一个单元格
                                colList.add("");
                                for(int k=0;k<colList.size();k++){
                                    System.out.print(colList.get(k));
                                }
                                System.out.println("");
                            }
                            continue;
                        }
                        //插入表头学科的逻辑
                        if(j>1){
                            ExamSubject examSubject  = new ExamSubject();
                            examSubject.setCreateTime(date);
                            examSubject.setEsIsDelete(Constants.IS_DELETE_N);
                            examSubject.setUpdateTime(date);
                            examSubject.setSubjectName(cell.getStringCellValue());
                            examSubjectList.add(examSubject);
                            excelSubjectList.add(cell.getStringCellValue());
                        }
                    }
                }else if(i==1){
                    if(row == null){
                        //当读取行为空时
                        if(i != sheet.getPhysicalNumberOfRows()){//判断是否是最后一行
                            rowList.add(colList);
                        }
                        continue;
                    }else{
                        rowCount++;
                    }
                    //行的读取逻辑
                    for( int j = row.getFirstCellNum() ; j <= row.getLastCellNum() ;j++){
                        cell = row.getCell(j);
                        if(cell == null || cell.getCellType() == HSSFCell.CELL_TYPE_BLANK){
                            //当该单元格为空
                            if(j != row.getLastCellNum()){//判断是否是该行中最后一个单元格
                                colList.add("");
                                for(int k=0;k<colList.size();k++){
                                    System.out.print(colList.get(k));
                                }
                                System.out.println("");
                            }
                            continue;
                        }
                        //判断满分是否符合规则
                        if(j>1){
                            if(cell.getCellType()==XSSFCell.CELL_TYPE_NUMERIC){
                                totalScoreList.add(new java.math.BigDecimal(cell.getNumericCellValue()));
                            }else{
                                totalScoreList.add(new BigDecimal(150));
                                ExcelWarnDto excelWarnDto = new ExcelWarnDto();
                                excelWarnDto.setColumn(i+1);
                                excelWarnDto.setRow(j+1);
                                excelWarnDto.setExplain("满分不符合规范");
                                excelWarnDtoList.add(excelWarnDto);
                                continue;
                            }
                        }
                        if(j>1&&examSubjectList.size()>j-2){
                            examSubjectList.get(j-2).setTotalScore(new java.math.BigDecimal(cell.getNumericCellValue()));
                            examSubjectList.get(j-2).setSubjectSort(j-1);
                        }
                    }
                }else{
                    if(row == null){
                        //当读取行为空时
                        if(i != sheet.getPhysicalNumberOfRows()){//判断是否是最后一行
                            rowList.add(colList);
                        }
                        continue;
                    }else{
                        rowCount++;
                    }
                    //行的读取逻辑
                    Long us_id = null;
                    String us_xjh = null;
                    String us_name = "";
                    Long usEducationid = null;
                    String us_code = null;
                    int mm = i;
                    String usName = null;
                    for( int j = row.getFirstCellNum() ; j <= row.getLastCellNum() ;j++){
                        if(j==-1)break;
                        cell = row.getCell(j);
                        if(cell!=null&&XSSFCell.CELL_TYPE_STRING==cell.getCellType()){
                            String cellStr = cell.getStringCellValue();
                            if(cellStr.indexOf("成绩模板填写说明")>=0){
                                cell_flag = true;
                                break;
                            }
                        }
                        if(cell == null || cell.getCellType() == HSSFCell.CELL_TYPE_BLANK){
                            //当该单元格为空
                            if(j != row.getLastCellNum()){//判断是否是该行中最后一个单元格
                                colList.add("");
                                for(int k=0;k<colList.size();k++){
                                    System.out.print(colList.get(k));
                                }
                                System.out.println("");
                            }
                            continue;
                        }
                        if(j==0){
                            int cellFlag = cell.getCellType();
                            if(XSSFCell.CELL_TYPE_NUMERIC==cellFlag){
                                usEducationid = new Long(Math.round(cell.getNumericCellValue()));
                                us_xjh = usEducationid.toString();
                                if(null==comList.getData()||comList.getData().size()==0||null==comList.getData().get(0)||null==comList.getData().get(0).getUsId()){
                                    ExcelWarnDto excelWarnDto = new ExcelWarnDto();
                                    excelWarnDto.setColumn(i+1);
                                    excelWarnDto.setRow(j+1);
                                    excelWarnDto.setExplain("教育ID或学号为"+us_xjh+"的学生不存在");
                                    excelWarnDtoList.add(excelWarnDto);
                                }else{
                                    boolean usEducationidFlag = true;
                                    for(int b =0;b<comList.getData().size();b++){
                                        if(us_xjh!=null&&(us_xjh.trim().equals(comList.getData().get(b).getUsXjh().trim())||us_xjh.trim().equals(comList.getData().get(b).getUsCode()))){
                                            us_id = comList.getData().get(b).getUsId();
                                            usName = comList.getData().get(b).getUsName();
                                            us_xjh = comList.getData().get(b).getUsXjh();
                                            us_code = comList.getData().get(b).getUsCode();
                                            usXjhList.add(comList.getData().get(b).getUsXjh());
                                            usEducationidFlag = false;
                                        }
                                    }
                                    if(usEducationidFlag){
                                        ExcelWarnDto excelWarnDto = new ExcelWarnDto();
                                        excelWarnDto.setColumn(i+1);
                                        excelWarnDto.setRow(j+1);
                                        excelWarnDto.setExplain("教育ID或学号为"+us_xjh+"的学生不存在");
                                        excelWarnDtoList.add(excelWarnDto);
                                    }
                                }
                            }else if(XSSFCell.CELL_TYPE_STRING==cellFlag){
                                us_xjh = cell.getStringCellValue();
                                if(null==comList.getData()||comList.getData().size()==0||null==comList.getData().get(0)||null==comList.getData().get(0).getUsId()){
                                    ExcelWarnDto excelWarnDto = new ExcelWarnDto();
                                    excelWarnDto.setColumn(i+1);
                                    excelWarnDto.setRow(j+1);
                                    excelWarnDto.setExplain("教育ID或学号为"+us_xjh+"的学生不存在");
                                    excelWarnDtoList.add(excelWarnDto);
                                }else{
                                    boolean usEducationidFlag = true;
                                    for(int b =0;b<comList.getData().size();b++){
                                        if(us_xjh!=null&&(us_xjh.trim().equals(comList.getData().get(b).getUsXjh().trim())||us_xjh.trim().equals(comList.getData().get(b).getUsCode()))){
                                            us_id = comList.getData().get(b).getUsId();
                                            usName = comList.getData().get(b).getUsName();
                                            us_xjh = comList.getData().get(b).getUsXjh();
                                            us_code = comList.getData().get(b).getUsCode();
                                            usXjhList.add(comList.getData().get(b).getUsXjh());
                                            usEducationidFlag = false;
                                        }
                                    }
                                    if(usEducationidFlag){
                                        ExcelWarnDto excelWarnDto = new ExcelWarnDto();
                                        excelWarnDto.setColumn(i+1);
                                        excelWarnDto.setRow(j+1);
                                        excelWarnDto.setExplain("教育ID或学号为"+us_xjh+"的学生不存在");
                                        excelWarnDtoList.add(excelWarnDto);
                                    }
                                }
                            }else{
                                ExcelWarnDto excelWarnDto = new ExcelWarnDto();
                                excelWarnDto.setColumn(i+1);
                                excelWarnDto.setRow(j+1);
                                excelWarnDto.setExplain("教育ID或学号为"+us_xjh+"的不符合规范");
                                excelWarnDtoList.add(excelWarnDto);
                            }
                            //==================================================
                        }
                        if(j==1){
                            us_name = cell.getStringCellValue();
                            if(usName!=null&&!usName.trim().equals(us_name.trim())){
                                ExcelWarnDto excelWarnDto = new ExcelWarnDto();
                                excelWarnDto.setColumn(i+1);
                                excelWarnDto.setRow(j+1);
                                excelWarnDto.setExplain("教育ID为"+us_xjh+"的学生姓名与名称“"+us_name+"”不相符");
                                excelWarnDtoList.add(excelWarnDto);
                            }else {
                                successRow += 1;
                            }
                        }
                        if(j>1){
                            if(cell.getCellType()!=XSSFCell.CELL_TYPE_NUMERIC){
                                ExcelWarnDto excelWarnDto = new ExcelWarnDto();
                                excelWarnDto.setColumn(i + 1);
                                excelWarnDto.setRow(j + 1);
                                excelWarnDto.setExplain("分数不符合规范");
                                excelWarnDtoList.add(excelWarnDto);
                                continue;
                            }
                            BigDecimal tmpScore = new java.math.BigDecimal(cell.getNumericCellValue());
                            int r = tmpScore.compareTo(BigDecimal.ZERO);
                            if(r==-1){
                                ExcelWarnDto excelWarnDto = new ExcelWarnDto();
                                excelWarnDto.setColumn(i + 1);
                                excelWarnDto.setRow(j + 1);
                                excelWarnDto.setExplain("分数不能小于0分");
                                excelWarnDtoList.add(excelWarnDto);
                                continue;
                            }
                            int scoreFlag = totalScoreList.get(j-2).compareTo(tmpScore);
                            if(scoreFlag == -1){
                                ExcelWarnDto excelWarnDto = new ExcelWarnDto();
                                excelWarnDto.setColumn(i + 1);
                                excelWarnDto.setRow(j + 1);
                                excelWarnDto.setExplain("分数不能大于该科目满分");
                                excelWarnDtoList.add(excelWarnDto);
                                continue;
                            }

                            Achievement achievement = new Achievement();
                            achievement.setAcIsDelete(Constants.IS_DELETE_N);
                            achievement.setCiId(ciId);
                            achievement.setCreatTime(date);
                            achievement.setUsId(us_id);
                            achievement.setUsName(us_name);
                            achievement.setSubjectName(excelSubjectList.get(j-2));
                            achievement.setScore(new java.math.BigDecimal(cell.getNumericCellValue()));
                            achievement.setAcIsDelete(Constants.IS_DELETE_N);
                            achievement.setCreatTime(date);
                            achievement.setUpdateTime(date);
                            achievement.setUsEducationid(us_xjh);
                            achievement.setUsCode(us_code);
                            achievementList.add(achievement);
                        }
                    }
                }
            }
            String[] arrSource = new String[examSubjectList.size()];
            for(int i=0;i<examSubjectList.size();i++){
                ExamSubject examSubject_I = examSubjectList.get(i);
                for(int j=0;j<examSubjectList.size();j++){
                    ExamSubject examSubject_J = examSubjectList.get(j);
                    if(examSubject_I!=null&&examSubject_J!=null&&examSubject_I.getSubjectName()!=null&& examSubject_J.getSubjectName()!=null&&i!=j&&examSubject_I.getSubjectName().equals(examSubject_J.getSubjectName())){
                        if(!examSubjectList.get(j).getSubjectName().equals(arrSource[j])) {
                            ExcelWarnDto excelWarnDto = new ExcelWarnDto();
                            excelWarnDto.setColumn(1);
                            excelWarnDto.setRow(j + 3);
                            excelWarnDto.setExplain("学科" + examSubject_I.getSubjectName() + "存在相同情况");
                            excelWarnDtoList.add(excelWarnDto);
                            arrSource[j] = examSubjectList.get(j).getSubjectName();
                        }
                    }

                }
            }
            String[] arrXjh = new String[usXjhList.size()];
            for(int i=0;i<usXjhList.size();i++){
                String  ux_xjh_I = usXjhList.get(i);
                for(int j=0;j<usXjhList.size();j++){
                    String  ux_xjh_J = usXjhList.get(j);
                    if(StringUtils.isNotBlank(ux_xjh_I)&&StringUtils.isNotBlank(ux_xjh_J)&&i!=j&&ux_xjh_I.equals(ux_xjh_J)){
                        if(!usXjhList.get(j).equals(arrXjh[j])){
                            ExcelWarnDto excelWarnDto = new ExcelWarnDto();
                            excelWarnDto.setColumn(j + 3);
                            excelWarnDto.setRow(1);
                            excelWarnDto.setExplain("教育ID"+ux_xjh_I+"存在相同情况");
                            excelWarnDtoList.add(excelWarnDto);
                            arrXjh[j] = usXjhList.get(j);
                        }
                    }
                }
            }
            if(excelWarnDtoList.size()>0){
                excelImportDto.setCode(MessageCode.EXCEL_IMPORT_FAIL.getCode());
                excelImportDto.setMessage(MessageCode.EXCEL_IMPORT_FAIL.getMessage());
                excelImportDto.setExcelWarnDtoList(excelWarnDtoList);
                rs.setData(excelImportDto);
            }else{
                //添加考试名称相同,删除前一次考试的逻辑
                Exam examBean = new Exam();
                examBean.setExName(exName);
                examBean.setExIsDelete(Constants.IS_DELETE_N);
                examBean.setCiId(ciId);
                List<Exam> examList = iExamService.findList(examBean);
                if(ciId!=null&&examList!=null&&examList.size()>0&&StringUtils.isNotBlank(exName)){
                    for(int i=0;i<examList.size();i++){
                        Boolean flag = iExamService.deleteExam(examList.get(i).getExId());
                    }
                }
                //添加考试成绩
                exam = iExamService.save(exam);
                //添加考试成绩对应的科目
                for(int i=0;i<examSubjectList.size();i++){
                    examSubjectList.get(i).setExId(exam.getExId());
                    iExamSubjectService.save(examSubjectList.get(i));
                }
                //添加考试成绩详情
                for(int i=0;i<achievementList.size();i++){
                    achievementList.get(i).setExId(exam.getExId());
                    iAchievementService.save(achievementList.get(i));
                }
                excelImportDto.setCode(MessageCode.EXCEL_IMPORT_SUCCESS.getCode());
                excelImportDto.setMessage(MessageCode.EXCEL_IMPORT_SUCCESS.getMessage()+",共导入"+(successRow)+"条");
                rs.setData(excelImportDto);
            }
        }catch(Exception e){
             e.printStackTrace();
        }
        return rs;
    }
    public static ArrayList<ArrayList<Object>> readExcel2003(File file){
        try{
            ArrayList<ArrayList<Object>> rowList = new ArrayList<ArrayList<Object>>();
            ArrayList<Object> colList;
//             file = new File("F:\\dongcheng\\3组织机构及职务信息.xls");
            HSSFWorkbook wb = new HSSFWorkbook(new FileInputStream(file));
            HSSFSheet sheet = wb.getSheetAt(0);
            HSSFRow row;
            HSSFCell cell;
            Object value;
            for(int i = sheet.getFirstRowNum() , rowCount = 0; rowCount < sheet.getPhysicalNumberOfRows() ; i++ ){
                row = sheet.getRow(i);
                colList = new ArrayList<Object>();
                if(row == null){
                    //当读取行为空时
                    if(i != sheet.getPhysicalNumberOfRows()){//判断是否是最后一行
                        rowList.add(colList);
                    }
                    continue;
                }else{
                    rowCount++;
                }
                for( int j = row.getFirstCellNum() ; j <= row.getLastCellNum() ;j++){
                    cell = row.getCell(j);
                    if(cell == null || cell.getCellType() == HSSFCell.CELL_TYPE_BLANK){
                        //当该单元格为空
                        if(j != row.getLastCellNum()){//判断是否是该行中最后一个单元格
                            colList.add("");
                            for(int k=0;k<colList.size();k++){
                                System.out.print(colList.get(k));
                            }
                            System.out.println("");
                        }
                        continue;
                    }
                    switch(cell.getCellType()){
                        case XSSFCell.CELL_TYPE_STRING:
//                                System.out.println(i + "行" + j + " 列 is String type");
                            value = cell.getStringCellValue();
                            break;
                        case XSSFCell.CELL_TYPE_NUMERIC:
                            if ("@".equals(cell.getCellStyle().getDataFormatString())) {
                                value = df.format(cell.getNumericCellValue());
                            } else if ("General".equals(cell.getCellStyle()
                                    .getDataFormatString())) {
                                value = nf.format(cell.getNumericCellValue());
                            } else {
                                value = sdf.format(HSSFDateUtil.getJavaDate(cell
                                        .getNumericCellValue()));
                            }
                            break;
                        case XSSFCell.CELL_TYPE_BOOLEAN:
                            //value = Boolean.valueOf(cell.getBooleanCellValue());
                            break;
                        case XSSFCell.CELL_TYPE_BLANK:
                            value = "";
                            break;
                        default:
                            value = cell.toString();
                    }// end switch
                    //colList.add(value);
                }//end for j
                rowList.add(colList);
            }//end for i

            return rowList;

        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }



    public static ArrayList<ArrayList<Object>> readExcel2007(File file){
        try{
            ArrayList<ArrayList<Object>> rowList = new ArrayList<ArrayList<Object>>();
            ArrayList<Object> colList;
            XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(file));
            XSSFSheet sheet = wb.getSheetAt(0);
            XSSFRow row;
            XSSFCell cell;
            Object value;
            for(int i = sheet.getFirstRowNum() , rowCount = 0; rowCount < sheet.getPhysicalNumberOfRows() ; i++ ){
                row = sheet.getRow(i);
                colList = new ArrayList<Object>();
                if(row == null){
                    //当读取行为空时
                    if(i != sheet.getPhysicalNumberOfRows()){//判断是否是最后一行
                        rowList.add(colList);
                    }
                    continue;
                }else{
                    rowCount++;
                }
                for( int j = row.getFirstCellNum() ; j <= row.getLastCellNum() ;j++){
                    cell = row.getCell(j);
                    if(cell == null || cell.getCellType() == HSSFCell.CELL_TYPE_BLANK){
                        //当该单元格为空
                        if(j != row.getLastCellNum()){//判断是否是该行中最后一个单元格
                            colList.add("");
                        }
                        continue;
                    }
                    switch(cell.getCellType()){
                        case XSSFCell.CELL_TYPE_STRING:
                            System.out.println(i + "行" + j + " 列 is String type");
                            value = cell.getStringCellValue();
                            break;
                        case XSSFCell.CELL_TYPE_NUMERIC:
                            if ("@".equals(cell.getCellStyle().getDataFormatString())) {
                                value = df.format(cell.getNumericCellValue());
                            } else if ("General".equals(cell.getCellStyle()
                                    .getDataFormatString())) {
                                value = nf.format(cell.getNumericCellValue());
                            } else {
                                value = sdf.format(HSSFDateUtil.getJavaDate(cell
                                        .getNumericCellValue()));
                            }
                            System.out.println(i + "行" + j
                                    + " 列 is Number type ; DateFormt:"
                                    + value.toString());
                            break;
                        case XSSFCell.CELL_TYPE_BOOLEAN:
                            System.out.println(i + "行" + j + " 列 is Boolean type");
                            //value = Boolean.valueOf(cell.getBooleanCellValue());
                            break;
                        case XSSFCell.CELL_TYPE_BLANK:
                            System.out.println(i + "行" + j + " 列 is Blank type");
                            value = "";
                            break;
                        default:
                            System.out.println(i + "行" + j + " 列 is default type");
                            value = cell.toString();
                    }// end switch
                    //colList.add(value);
                }//end for j
                rowList.add(colList);
            }//end for i

            return rowList;
        }catch(Exception e){
            System.out.println("exception");
            e.printStackTrace();
            return null;
        }
    }
    private static DecimalFormat df = new DecimalFormat("0");
    // 默认单元格格式化日期字符串
    private static SimpleDateFormat sdf = new SimpleDateFormat(  "yyyy-MM-dd HH:mm:ss");
    // 格式化数字
    private static DecimalFormat nf = new DecimalFormat("0.00");
}
