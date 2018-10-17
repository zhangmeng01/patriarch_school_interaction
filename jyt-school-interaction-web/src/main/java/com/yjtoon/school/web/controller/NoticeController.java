package com.yjtoon.school.web.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.jyt.user.service.IClassService;
import com.jyt.user.service.IOrganizationService;
import com.jyt.user.service.IStudentService;
import com.jyt.user.service.IToonUserService;
import com.jyt.user.service.IUserService;
import com.jyt.user.service.entity.ClassInfo;
import com.jyt.user.service.entity.ClassTeacherRel;
import com.jyt.user.service.entity.Grade;
import com.jyt.user.service.entity.StudentParentRel;
import com.jyt.user.service.entity.TNPToonCard;
import com.jyt.user.service.entity.ToonCardEntity;
import com.jyt.user.service.entity.UserParent;
import com.jyt.user.service.entity.UserStudent;
import com.jyt.user.service.entity.UserTeacher;
import com.jyt.user.service.enums.UserType;
import com.jyt.user.service.response.CommonResponse;
import com.jyt.user.service.vo.StudentParentRelVO;
import com.jyt.xiaotoon.admin.service.ClassCourseTeacherRelService;
import com.jyt.xiaotoon.admin.service.CourseTeacherRelService;
import com.jyt.xiaotoon.admin.service.entity.ClassCourseTeacherRelBean;
import com.jyt.xiaotoon.admin.service.entity.CourseTeacherRelBean;
import com.yjtoon.framework.annotations.LogWrapper;
import com.yjtoon.framework.annotations.ReturnWrapper;
import com.yjtoon.framework.exception.ReturnInfo;
import com.yjtoon.framework.sqlplus.EntityWrapper;
import com.yjtoon.framework.tooncode.ToonCode;
import com.yjtoon.framework.tooncode.ToonVisitor;
import com.yjtoon.school.IAchievementService;
import com.yjtoon.school.IExamService;
import com.yjtoon.school.INoticePictureService;
import com.yjtoon.school.INoticeRangeService;
import com.yjtoon.school.INoticeService;
import com.yjtoon.school.domain.Exam;
import com.yjtoon.school.domain.Notice;
import com.yjtoon.school.domain.NoticePicture;
import com.yjtoon.school.domain.NoticeRange;
import com.yjtoon.school.dto.MyScoreAndRankDto;
import com.yjtoon.school.dto.ParentDto;
import com.yjtoon.school.dto.StudentDto;
import com.yjtoon.school.util.Constants;
import com.yjtoon.school.util.DateUtils;
import com.yjtoon.school.vo.NoticeExtVO;
import com.yjtoon.school.vo.NoticeListVO;
import com.yjtoon.school.vo.NoticeRangeExtVO;
import com.yjtoon.school.vo.NoticeVO;
import com.yjtoon.school.vo.UserBean;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@LogWrapper
@ReturnWrapper
@Validated
@RestController
@RequestMapping("/jxhd/notice")
@Api(value = "/jxhd/notice", description = "发布通知")
public class NoticeController {

	private static final Logger logger = LoggerFactory.getLogger(NoticeController.class);

	/*
	 * @Autowired private AppInfoConfig appInfoConfig;
	 */
	@Autowired
	private YearController yearController;
	@Autowired
	private IAchievementService iAchievementService;
	@Autowired
	private INoticeService noticeService;
	@Autowired
	private INoticeRangeService noticeRangeService;
	@Autowired
	private INoticePictureService noticePictureService;
	@Autowired
	private IExamService examService;
	@Reference(version = "1.0.0")
	private IStudentService studentService;
	@Reference(version = "1.0.0")
	private IOrganizationService organizationService;
	@Reference(version = "1.0.0")
	private IClassService classService;
	@Reference(version = "1.0.0")
	private IUserService userService;
	@Reference(version = "1.0.0")
	private CourseTeacherRelService courseTeacherRelService;
	@Reference(version = "1.0.0")
	private ClassCourseTeacherRelService classCourseTeacherRelService;
	@Reference(version = "1.0.0")
	private IToonUserService toonUserService;

	/*
	 * @InitBinder private void initBinder(WebDataBinder binder){
	 * //由表单到JavaBean赋值过程中日期格式转换 binder.registerCustomEditor(Date.class, new
	 * CustomDateEditor(new SimpleDateFormat(DateUtils.DATE_FORMAT_DATETIME),
	 * true)); }
	 */

	@RequestMapping(value = "/publish", method = RequestMethod.POST)
	@ApiOperation(value = "发布通知(包含通知、作业、成绩)", httpMethod = "POST")
	@ApiImplicitParam(paramType = "form", value = "封装前端form表单提交内容 ", name = "noticeVO", required = true, dataType = "String")
	@Transactional
	public ReturnInfo<Boolean> publishNotice(@RequestBody NoticeVO noticeVO, HttpServletRequest request) {
		// 通知信息
		Notice notice = noticeVO.getNotice();
		logger.info("获取到的回执时间===============" + notice.getNoReceiptTime());
		// 根据类型设置title
		Integer type = notice.getNoType();
		if (type == Constants.NOTICE_HOMEWORK) {
			notice.setNoTitle(notice.getNoSubject() + Constants.HOMEWORK);
		} else if (type == Constants.NOTICE_SCORE) {
			Exam exam = examService.getById(notice.getNoExamId());
			if (exam != null) {
				notice.setNoTitle(exam.getExName());
				notice.setNoContent(exam.getExName());
			}
		}
		// 发布人ID
		notice.setNoPublisherId(getUserId(request));
		// 保存之后返回的通知信息
		Notice noticeReturn = noticeService.save(notice);
		// 判断通知是否有图片,如果有则保存通知图片信息
		if (notice.getNoPicture() == 1) {
			List<NoticePicture> noticePictures = noticeVO.getNoticePictures();
			for (NoticePicture noticePicture : noticePictures) {
				noticePicture.setNoId(noticeReturn.getNoId());
				noticePictureService.save(noticePicture);
			}
		}
		// 通知范围
		List<NoticeRange> noticeRanges = noticeVO.getNoticeRanges();
		for (NoticeRange noticeRange : noticeRanges) {
			noticeRange.setNoId(noticeReturn.getNoId());
			noticeRangeService.save(noticeRange);
			// 发布通知
			publishByToon(noticeReturn, noticeRange, request);
		}
		ReturnInfo<Boolean> ri = new ReturnInfo<>();
		ri.setData(true);
		return ri;
	}

	@RequestMapping(value = "/classes/list", method = RequestMethod.GET)
	@ApiOperation(value = "获取筛选班级列表", httpMethod = "GET")
	public ReturnInfo<List<Map<String, Object>>> getClasses(HttpServletRequest request) {
		ReturnInfo<List<Map<String, Object>>> ri = new ReturnInfo<>();
		List<Map<String, Object>> classes = new ArrayList<>();
		CommonResponse<List<ClassInfo>> crList = classService.findClassByTeacher(getFeedId(request));
		List<ClassInfo> data = crList.getData();
		if (data != null && data.size() > 0) {
			for (ClassInfo ci : data) {
				CommonResponse<Grade> cr = organizationService.findGradeById(ci.getGrId());
				Grade grade = cr.getData();
				String className = null;
				if (grade != null) {
					className = grade.getGrName() + ci.getCiName();
				}
				Map<String, Object> map = new HashMap<>();
				map.put("classId", ci.getCiId());
				map.put("className", className);
				classes.add(map);
			}
		}
		ri.setData(classes);
		return ri;
	}

	@RequestMapping(value = "/subjects/list", method = RequestMethod.GET)
	@ApiOperation(value = "获取科目列表", httpMethod = "GET")
	public ReturnInfo<Map<String, List<Map<String, Object>>>> getSubjects(HttpServletRequest request) {
		ReturnInfo<Map<String, List<Map<String, Object>>>> ri = new ReturnInfo<>();
		Map<String, List<Map<String, Object>>> result = new HashMap<>();
		// 任科老师科目列表
		List<Map<String, Object>> teacherSubjectList = new ArrayList<>();
		// 去重存放科目
		Map<String, Map<String, Object>> teacherSubjects = new HashMap<>();
		Long userId = getUserId(request);
		CourseTeacherRelBean crt = new CourseTeacherRelBean();
		crt.setUtId(userId);
		logger.info("用户ID信息[" + userId + "]");
		List<CourseTeacherRelBean> list = courseTeacherRelService.findList(crt);
		logger.info("课程老师关系列表信息[" + list + "]");
		if (list != null && list.size() > 0) {
			for (CourseTeacherRelBean courseTeacherRelBean : list) {
				Map<String, Object> map = new HashMap<>();
				map.put("subjectId", courseTeacherRelBean.getCourId());
				map.put("subjectName", courseTeacherRelBean.getCctCourName());
				// 去重
				teacherSubjects.put(courseTeacherRelBean.getCctCourName(), map);
				// teacherSubjectList.add(map);
			}
		}
		logger.info("去重后任课老师科目列表信息[" + teacherSubjects + "]");
		// map转list
		for (Map.Entry<String, Map<String, Object>> entry : teacherSubjects.entrySet()) {
			teacherSubjectList.add(entry.getValue());
		}
		Collections.sort(teacherSubjectList, new Comparator<Map<String, Object>>() {
			@Override
			public int compare(Map<String, Object> o1, Map<String, Object> o2) {
				String subjectName1 = (String) o1.get("subjectName");
				String subjectName2 = (String) o2.get("subjectName");
				return subjectName1.compareTo(subjectName2);
			}
		});
		logger.info("排序后任课老师科目列表信息[" + teacherSubjects + "]");
		// 班主任科目列表
		List<Map<String, Object>> masterSubjectList = new ArrayList<>();
		// 去重存放科目
		Map<String, Map<String, Object>> masterSubjects = new HashMap<>();
		ClassTeacherRel classTeacherRel = getClassTeacherRel(userId);
		if (classTeacherRel != null) {
			Long ciId = classTeacherRel.getCiId();
			logger.info("班主任所带班级ID信息[" + ciId + "]");
			ClassCourseTeacherRelBean cctr = new ClassCourseTeacherRelBean();
			cctr.setcId(ciId);
			List<ClassCourseTeacherRelBean> cctrList = classCourseTeacherRelService.findList(cctr);
			logger.info("班级课程老师关系列表信息[" + cctrList + "]");
			for (ClassCourseTeacherRelBean classCourseTeacherRelBean : cctrList) {
				Map<String, Object> map = new HashMap<>();
				Long ctId = classCourseTeacherRelBean.getCctId();
				CourseTeacherRelBean courseTeacherRelBean = courseTeacherRelService.getById(ctId);
				map.put("subjectId", courseTeacherRelBean.getCourId());
				map.put("subjectName", courseTeacherRelBean.getCctCourName());
				// 去重
				masterSubjects.put(courseTeacherRelBean.getCctCourName(), map);
				// masterSubjectList.add(map);
			}
		}
		logger.info("去重后班主任科目列表信息[" + teacherSubjects + "]");
		// mapz转list
		for (Map.Entry<String, Map<String, Object>> entry : masterSubjects.entrySet()) {
			masterSubjectList.add(entry.getValue());
		}
		// 对科目排序保证科目每次顺序一致
		Collections.sort(masterSubjectList, new Comparator<Map<String, Object>>() {
			@Override
			public int compare(Map<String, Object> o1, Map<String, Object> o2) {
				String subjectName1 = (String) o1.get("subjectName");
				String subjectName2 = (String) o2.get("subjectName");
				return subjectName1.compareTo(subjectName2);
			}
		});
		logger.info("排序后班主任科目列表信息[" + teacherSubjects + "]");
		result.put("teacherSubjectList", teacherSubjectList);
		result.put("masterSubjectList", masterSubjectList);
		ri.setData(result);
		return ri;
	}

	private ClassTeacherRel getClassTeacherRel(Long userId) {
		ClassTeacherRel classteacherrel = new ClassTeacherRel();
		classteacherrel.setUtId(userId);
		classteacherrel.setCtrIsHeadmaster(Constants.IS_MASTER_TEACHER_Y);
		CommonResponse<List<ClassTeacherRel>> classTeacherRelList = userService
				.findClassTeacherRelList(classteacherrel);
		List<ClassTeacherRel> data = classTeacherRelList.getData();
		if (data != null && data.size() > 0) {
			return data.get(0);
		}
		return null;
	}

	@RequestMapping(value = "/noticeRange/list", method = RequestMethod.GET)
	@ApiOperation(value = "获取通知发布范围", httpMethod = "GET")
	public ReturnInfo<List<Map<String, Object>>> findNoticeRange(HttpServletRequest request, Integer noType) {
		ReturnInfo<List<Map<String, Object>>> rs = new ReturnInfo<List<Map<String, Object>>>();
		List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
		// 获取班级列表
		String feedId = getFeedId(request);
		CommonResponse<List<ClassInfo>> crList = classService.findClassByTeacher(feedId);
		// 非作业通知 只能发给班主任所在的班级
		Long utId = getUserId(request);
		logger.info("utId["+utId+"]");
		List<ClassInfo> classList = crList.getData();
		for(ClassInfo classInfo : classList) {
			logger.info("老师班级关系列表["+classInfo.getCiGrName()+"@@@@班级对应班主任ID"+classInfo.getCiHeadmasterId()+"]");
		}
		if (utId != null && Constants.NOTICE_HOMEWORK != noType) {
			List<ClassInfo> classInfoList = new ArrayList<>();
			for (ClassInfo classInfo : classList) {
				Long ciHeadmasterId = classInfo.getCiHeadmasterId();
				if (ciHeadmasterId != null && utId.longValue() == ciHeadmasterId.longValue()) {
					logger.info("班主任班级信息["+classInfo.getCiGrName()+"]");
					classInfoList.add(classInfo);
				}
			}
			classList = classInfoList;
		}
		if (classList != null && classList.size() > 0) {
			for (ClassInfo classInfo : classList) {
				Long grId = classInfo.getGrId();// 年级ID
				Long ciId = classInfo.getCiId();// 班级ID
				// 获取年级信息
				CommonResponse<Grade> comGrade = organizationService.findGradeById(classInfo.getGrId());
				String gradeName = comGrade.getData().getGrName();// 年级名称
				String className = classInfo.getCiName();// 班级名称
				String studentLable = gradeName + className + Constants.STUDENT_GROUP;// 学生群名称
				// 学生的发布范围
				List<StudentDto> studentDtoList = new ArrayList<StudentDto>();
				// 学生ids
				List<Long> stuIdList = new ArrayList<Long>();
				// 学生列表
				CommonResponse<List<UserStudent>> comUserStudentList = classService.findStudentClassByClassInfo(ciId);
				List<UserStudent> usList = comUserStudentList.getData();
				if (usList != null && usList.size() > 0) {
					for (UserStudent us : usList) {
						StudentDto studentDto = new StudentDto();
						studentDto.setNrUserId(us.getUsId());
						studentDto.setNrUserName(us.getUsName());
						studentDto.setNrType(Constants.STUDENT_TYPE);
						studentDto.setCiId(ciId);
						studentDtoList.add(studentDto);
						// 组装学生ids
						stuIdList.add(us.getUsId());
					}
				}
				Map<String, Object> mapStudent = new HashMap<String, Object>();
				mapStudent.put("grId", grId);
				mapStudent.put("ciId", ciId);
				mapStudent.put("id", ciId + "_stu");
				mapStudent.put("nrUserName", studentLable);
				mapStudent.put("children", studentDtoList);
				mapList.add(mapStudent);

				// 家长的发布范围
				List<ParentDto> parentDtoList = new ArrayList<ParentDto>();
				// 家长ids
				List<Long> parIdList = new ArrayList<Long>();
				// 学生家长关系列表
				CommonResponse<List<StudentParentRel>> comStudentParentRalList = userService.findStudentParentRelByUsIds(stuIdList.toArray(new Long[stuIdList.size()]));
				List<StudentParentRel> spRelList = comStudentParentRalList.getData();
				if (spRelList != null && spRelList.size() > 0) {
					for (StudentParentRel sp : spRelList) {
						// 组装家长ids
						parIdList.add(sp.getUpId());
					}
				}
				// 家长列表
				CommonResponse<List<UserParent>> comUserParentList = userService.findUserParentByIds(parIdList.toArray(new Long[parIdList.size()]));
				List<UserParent> upList = comUserParentList.getData();
				if (upList != null && upList.size() > 0) {
					for (UserParent up : upList) {
						ParentDto parentDto = new ParentDto();
						parentDto.setNrUserId(up.getUpId());
						parentDto.setNrUserName(up.getUpName());
						parentDto.setNrType(Constants.PARENT_TYPE);
						parentDto.setCiId(ciId);
						parentDtoList.add(parentDto);
					}
				}
				String parentLable = gradeName + className + Constants.PARENT_GROUP;// 家长群名称
				Map<String, Object> mapParent = new HashMap<String, Object>();
				mapParent.put("grId", grId);
				mapParent.put("ciId", ciId);
				mapParent.put("id", ciId + "_par");
				mapParent.put("nrUserName", parentLable);
				mapParent.put("children", parentDtoList);
				mapList.add(mapParent);
			}
		}
		// 按年级、班级排序
		Collections.sort(mapList, new Comparator<Map<String, Object>>() {
			@Override
			public int compare(Map<String, Object> o1, Map<String, Object> o2) {
				Long grId1 = (Long) o1.get("grId");
				Long grId2 = (Long) o2.get("grId");
				Long ciId1 = (Long) o1.get("ciId");
				Long ciId2 = (Long) o2.get("ciId");
				if (grId1.compareTo(grId2) > 0) {
					return 1;
				} else if (grId1.compareTo(grId2) == 0) {
					if (ciId1.compareTo(ciId2) > 0) {
						return 1;
					} else if (ciId1.compareTo(ciId2) == 0) {
						return 0;
					} else {
						return -1;
					}
				} else {
					return -1;
				}
			}
		});
		rs.setData(mapList);
		return rs;
	}

	@RequestMapping(value = "/list/publisher", method = RequestMethod.GET)
	@ApiOperation(value = "根据筛选条件获取通知列表(我发布的)", httpMethod = "GET")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", value = "班级ID", name = "classIds", required = false, dataType = "String"),
			@ApiImplicitParam(paramType = "query", value = "通知类型(0-通知 1-作业 2-成绩)", name = "noTypes", required = false, dataType = "String"),
			@ApiImplicitParam(paramType = "query", value = "分页页数", name = "pageNumber", required = true, dataType = "int"),
			@ApiImplicitParam(paramType = "query", value = "分页大小", name = "pageSize", required = true, dataType = "int") })
	public ReturnInfo<PageInfo<NoticeListVO>> getNoticeByFilterForPublisher(HttpServletRequest request, String classIds,
			String noTypes, int pageNumber, int pageSize) {
		ReturnInfo<PageInfo<NoticeListVO>> ri = new ReturnInfo<>();
		Map<String, List<Notice>> resultMap = new LinkedHashMap<>();
		List<Long> classIdList = new ArrayList<>();
		List<Integer> noTypeList = new ArrayList<>();
		if (StringUtils.isNotEmpty(classIds)) {
			String[] classesArray = classIds.split(",");
			for (String s : classesArray) {
				classIdList.add(Long.parseLong(s));
			}
		}
		if (StringUtils.isNotEmpty(noTypes)) {
			String[] noTypesArray = noTypes.split(",");
			for (String s : noTypesArray) {
				noTypeList.add(Integer.parseInt(s));
			}
		}
		Long currentUserId = getUserId(request);
		// 获取符合条件的通知列表(分页)
		List<NoticeExtVO> notices = noticeService.getNoticeByFilterForPublisher(currentUserId, classIdList, noTypeList,
				pageNumber, pageSize);
		for (NoticeExtVO notice : notices) {
			// 通知发布的班级名称
			String className = getClassName(notice.getNoId());
			notice.setClassName(className);
		}
		// 分组
		for (Notice notice : notices) {
			String dateInfo = DateUtils.date2String(notice.getCreateTime(), DateUtils.DATE_FORMAT_DATEONLY);
			String weekInfo = DateUtils.dateToWeek(dateInfo);
			String groupInfo = dateInfo + " " + weekInfo;
			List<Notice> tempList = resultMap.get(groupInfo);
			if (tempList == null) {
				tempList = new ArrayList<>();
				tempList.add(notice);
				resultMap.put(groupInfo, tempList);
			} else {
				tempList.add(notice);
			}
		}
		List<NoticeListVO> list = new ArrayList<>();
		// 转为list
		for (Map.Entry<String, List<Notice>> entry : resultMap.entrySet()) {
			NoticeListVO vo = new NoticeListVO();
			vo.setDateInfo(entry.getKey());
			vo.setNotices(entry.getValue());
			list.add(vo);
		}
		// 分页信息处理
		PageInfo<NoticeExtVO> pi = new PageInfo<>(notices);
		PageInfo<NoticeListVO> pageInfo = new PageInfo<>();
		try {
			BeanUtils.copyProperties(pageInfo, pi);
		} catch (Exception e) {
			logger.error("属性拷贝异常");
			e.printStackTrace();
		}
		pageInfo.setList(list);
		ri.setData(pageInfo);
		return ri;
	}

	@RequestMapping(value = "/list/receiver", method = RequestMethod.GET)
	@ApiOperation(value = "根据筛选条件获取通知列表(我接收的)", httpMethod = "GET")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", value = "通知类型(0-通知 1-作业 2-成绩)", name = "noTypes", required = false, dataType = "String"),
			@ApiImplicitParam(paramType = "query", value = "分页页数", name = "pageNumber", required = true, dataType = "int"),
			@ApiImplicitParam(paramType = "query", value = "分页大小", name = "pageSize", required = true, dataType = "int") })
	public ReturnInfo<PageInfo<NoticeListVO>> getNoticeByFilterForReceiver(HttpServletRequest request, String noTypes,
			int pageNumber, int pageSize) {
		ReturnInfo<PageInfo<NoticeListVO>> ri = new ReturnInfo<>();
		Map<String, List<Notice>> resultMap = new LinkedHashMap<>();
		List<Integer> noTypeList = new ArrayList<>();
		if (StringUtils.isNotEmpty(noTypes)) {
			String[] noTypesArray = noTypes.split(",");
			for (String s : noTypesArray) {
				noTypeList.add(Integer.parseInt(s));
			}
		}
		Long currentUserId = getUserId(request);
		Integer userType = getUserType(request);
		// 获取符合条件的通知列表(分页)
		List<NoticeExtVO> notices = noticeService.getNoticeByFilterForReceiver(currentUserId, noTypeList, userType,
				pageNumber, pageSize);
		for (NoticeExtVO notice : notices) {
			// 通知发布的班级名称
			String publisher = getPublisher(notice.getNoPublisherId());
			notice.setPublisher(publisher);
		}
		// 分组
		for (Notice notice : notices) {
			String dateInfo = DateUtils.date2String(notice.getCreateTime(), DateUtils.DATE_FORMAT_DATEONLY);
			String weekInfo = DateUtils.dateToWeek(dateInfo);
			String groupInfo = dateInfo + " " + weekInfo;
			List<Notice> tempList = resultMap.get(groupInfo);
			if (tempList == null) {
				tempList = new ArrayList<>();
				tempList.add(notice);
				resultMap.put(groupInfo, tempList);
			} else {
				tempList.add(notice);
			}
		}
		List<NoticeListVO> list = new ArrayList<>();
		for (Map.Entry<String, List<Notice>> entry : resultMap.entrySet()) {
			NoticeListVO vo = new NoticeListVO();
			vo.setDateInfo(entry.getKey());
			vo.setNotices(entry.getValue());
			list.add(vo);
		}

		// 处理分页信息
		PageInfo<NoticeExtVO> pi = new PageInfo<>(notices);
		PageInfo<NoticeListVO> pageInfo = new PageInfo<>();
		try {
			BeanUtils.copyProperties(pageInfo, pi);
		} catch (Exception e) {
			logger.error("属性拷贝异常");
			e.printStackTrace();
		}
		pageInfo.setList(list);
		ri.setData(pageInfo);
		return ri;
	}

	@RequestMapping(value = "/receipt", method = RequestMethod.POST)
	@ApiOperation(value = "回执通知", httpMethod = "POST")
	@ApiImplicitParam(paramType = "query", value = "通知ID", name = "noticeId", required = true, dataType = "long")
	@Transactional
	public ReturnInfo<Boolean> receiptNotice(long noticeId, HttpServletRequest request) {
		ReturnInfo<Boolean> ri = new ReturnInfo<>();
		Long currentUserId = getUserId(request);
		boolean flag = noticeRangeService.receiptNotice(noticeId, currentUserId);
		ri.setData(flag);
		if (flag) {
			logger.info("回执成功 ");
		} else {
			logger.info("回执失败 ");
		}
		return ri;
	}

	@RequestMapping(value = "/remind/single", method = RequestMethod.POST)
	@ApiOperation(value = "通知个人提醒", httpMethod = "POST")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", value = "通知ID", name = "noticeId", required = true, dataType = "long"),
			@ApiImplicitParam(paramType = "query", value = "提醒用户ID", name = "userId", required = true, dataType = "long"),
			@ApiImplicitParam(paramType = "query", value = "提醒用户类型(0-学生 1-家长 2-老师)", name = "userType", required = true, dataType = "long"), })
	@Transactional
	public ReturnInfo<Boolean> remindNoticeSingle(long noticeId, HttpServletRequest request, long userId,
			int userType) {
		ReturnInfo<Boolean> ri = new ReturnInfo<>();
		Notice notice = noticeService.getById(noticeId);
		NoticeRange nr = new NoticeRange();
		nr.setNoId(noticeId);
		nr.setNrUserId(userId);
		nr.setNrType(userType);
		nr.setNrRemind(0);
		NoticeRange noticeRange = noticeRangeService.get(nr);
		boolean flag = noticeRangeService.remindNoticeSingle(noticeId, userId);
		ri.setData(flag);
		if (flag) {
			// 调用toon发消息
			publishByToon(notice, noticeRange, request);
			logger.info("提醒成功 ");
		} else {
			logger.info("提醒失败 ");
		}
		return ri;
	}

	@RequestMapping(value = "/remind/all", method = RequestMethod.POST)
	@ApiOperation(value = "通知一键全部提醒(已提醒的除外)", httpMethod = "POST")
	@ApiImplicitParam(paramType = "query", value = "通知ID", name = "noticeId", required = true, dataType = "long")
	public ReturnInfo<Boolean> remindNoticeAll(long noticeId, HttpServletRequest request) {
		ReturnInfo<Boolean> ri = new ReturnInfo<>();
		Notice notice = noticeService.getById(noticeId);
		// 查询未提醒的人员
		EntityWrapper ew = new EntityWrapper();
		Map<String, Object> conditions = new HashMap<>();
		conditions.put("no_id", noticeId);
		conditions.put("nr_remind", 0);
		conditions.put("nr_receipt", 0);
		ew.allEq(conditions);
		List<NoticeRange> list = noticeRangeService.findListByWrapper(ew);
		if (list != null && list.size() > 0) {
			boolean flag = noticeRangeService.remindNoticeAll(noticeId);
			if (flag) {
				for (NoticeRange noticeRange : list) {
					publishByToon(notice, noticeRange, request);
				}
				ri.setData(flag);
				logger.info("一键提醒成功 ");
			}
		} else {
			logger.info("一键提醒失败 ");
		}
		return ri;
	}

	@RequestMapping(value = "/noticeDetail/publisher/withReceipt", method = RequestMethod.GET)
	@ApiOperation(value = "通知发布详情查看(发布人查看且需回执)", httpMethod = "GET")
	@ApiImplicitParam(paramType = "query", value = "通知ID", name = "noticeId", required = true, dataType = "long")
	public ReturnInfo<Map<String, Object>> noticeDetailForPublisherWithReceipt(long noticeId) {
		ReturnInfo<Map<String, Object>> ri = new ReturnInfo<>();
		// 已回执人数
		int receiptCount = getPersonCount(noticeId, 1, 1);
		// 已回执人数列表;已回执默认按回执时间排序
		List<NoticeRangeExtVO> receiptPersonList = noticeRangeService.getReceiptPersonList(noticeId);
		setNameAndIcon(receiptPersonList);
		// 未回执人数
		int unReceiptCount = getPersonCount(noticeId, 1, 0);
		// 未回执人数列表
		List<NoticeRangeExtVO> unReceiptPersonList = noticeRangeService.getUnReceiptPersonList(noticeId);
		setNameAndIcon(unReceiptPersonList);
		// 未回执列表按姓名排序
		Collections.sort(unReceiptPersonList, new Comparator<NoticeRangeExtVO>() {
			@Override
			public int compare(NoticeRangeExtVO o1, NoticeRangeExtVO o2) {
				return o1.getName().compareTo(o2.getName());
			}

		});
		// 通知信息
		Notice noticeInfo = noticeService.getById(noticeId);
		// 通知发布的班级名称
		String className = getClassName(noticeId);
		Map<String, Object> result = new HashMap<>();
		result.put("processedPersonCount", receiptCount);
		result.put("processedpersonList", receiptPersonList);
		result.put("unProcessedPersonCount", unReceiptCount);
		result.put("unprocessedPersonList", unReceiptPersonList);
		if (noticeInfo.getNoPicture() == 1) {
			NoticePicture np = new NoticePicture();
			np.setNoId(noticeInfo.getNoId());
			List<NoticePicture> nps = noticePictureService.findList(np);
			result.put("pics", nps);
		}
		result.put("noticeInfo", noticeInfo);
		result.put("className", className);
		ri.setData(result);
		return ri;
	}

	@RequestMapping(value = "/noticeDetail/publisher/withoutReceipt", method = RequestMethod.GET)
	@ApiOperation(value = "通知发布详情查看(发布人查看且无需回执)", httpMethod = "GET")
	@ApiImplicitParam(paramType = "query", value = "通知ID", name = "noticeId", required = true, dataType = "long")
	public ReturnInfo<Map<String, Object>> noticeDetailForPublisherWithoutReceipt(long noticeId) {
		ReturnInfo<Map<String, Object>> ri = new ReturnInfo<>();
		// 已查阅人数
		int consultCount = getPersonCount(noticeId, 0, 1);
		// 已查阅人数列表;已查阅列表按查阅时间排序
		List<NoticeRangeExtVO> consultPersonList = noticeRangeService.getConsultPersonList(noticeId);
		setNameAndIcon(consultPersonList);
		// 未查阅人数
		int unConsultCount = getPersonCount(noticeId, 0, 0);
		// 未查阅人数列表
		List<NoticeRangeExtVO> unConsultPersonList = noticeRangeService.getUnConsultPersonList(noticeId);
		setNameAndIcon(unConsultPersonList);
		// 未查阅列表按姓名排序
		Collections.sort(unConsultPersonList, new Comparator<NoticeRangeExtVO>() {
			@Override
			public int compare(NoticeRangeExtVO o1, NoticeRangeExtVO o2) {
				return o1.getName().compareTo(o2.getName());
			}

		});
		// 通知信息
		Notice noticeInfo = noticeService.getById(noticeId);
		// 通知发布的班级名称
		String className = getClassName(noticeId);
		Map<String, Object> result = new HashMap<>();
		result.put("processedPersonCount", consultCount);
		result.put("processedpersonList", consultPersonList);
		result.put("unProcessedPersonCount", unConsultCount);
		result.put("unprocessedPersonList", unConsultPersonList);
		if (noticeInfo.getNoPicture() == 1) {
			NoticePicture np = new NoticePicture();
			np.setNoId(noticeInfo.getNoId());
			List<NoticePicture> nps = noticePictureService.findList(np);
			result.put("pics", nps);
		}
		result.put("noticeInfo", noticeInfo);
		result.put("className", className);
		ri.setData(result);
		return ri;
	}

	private void setNameAndIcon(List<NoticeRangeExtVO> list) {
		for (NoticeRangeExtVO nre : list) {
			if (nre.getNrType() == Constants.STUDENT_TYPE) {
				CommonResponse<UserStudent> cr = userService.findUserStudentById(nre.getNrUserId());
				UserStudent stu = cr.getData();
				nre.setName(stu.getUsName());
				nre.setIconUrl(stu.getUsIconUrl());
			} else if (nre.getNrType() == Constants.PARENT_TYPE) {
				CommonResponse<UserParent> cr = userService.findUserParentById(nre.getNrUserId());
				UserParent parent = cr.getData();
				nre.setName(parent.getUpName());
				nre.setIconUrl(parent.getUpIconUrl());
			}
		}
	}

	@RequestMapping(value = "/noticeDetail/reveiver", method = RequestMethod.GET)
	@ApiOperation(value = "通知发布详情查看(接收人查看)", httpMethod = "GET")
	@ApiImplicitParam(paramType = "query", value = "通知ID", name = "noticeId", required = true, dataType = "long")
	@Transactional
	public ReturnInfo<Map<String, Object>> noticeDetailForReceiver(long noticeId, HttpServletRequest request) {
		ReturnInfo<Map<String, Object>> ri = new ReturnInfo<>();
		NoticeRange noticeRange = null;
		Long currentUserId = getUserId(request);// 默认从session中获得
		Integer userType = getUserType(request);// 默认从session中获得
		if (currentUserId != null) {
			// 获取查阅状态
			NoticeRange nr = new NoticeRange();
			nr.setNoId(noticeId);
			nr.setNrUserId(currentUserId);
			nr.setNrType(userType);
			noticeRange = noticeRangeService.get(nr);
			// 未查阅 则修改查阅状态及时间
			if (noticeRange != null && noticeRange.getNrConsult() == 0) {
				noticeRangeService.consultNotice(noticeId, currentUserId);
			}
		}
		// 通知信息
		Notice noticeInfo = noticeService.getById(noticeId);
		NoticeExtVO nev = new NoticeExtVO();
		try {
			BeanUtils.copyProperties(nev, noticeInfo);
		} catch (Exception e) {
			logger.error("属性拷贝异常");
			e.printStackTrace();
		}
		if (noticeRange != null) {
			nev.setNrReceipt(noticeRange.getNrReceipt());
		}
		// 通知发布的发布人名称
		String publisher = getPublisher(noticeInfo.getNoPublisherId());
		Map<String, Object> result = new HashMap<>();
		result.put("noticeInfo", nev);
		result.put("publisher", publisher);
		result.put("currentUserFeedId", getFeedId(request));
		result.put("publisherFeedId", getFeedId(nev.getNoPublisherId(), 2));
		if (noticeInfo.getNoPicture() == 1) {
			NoticePicture np = new NoticePicture();
			np.setNoId(noticeInfo.getNoId());
			List<NoticePicture> nps = noticePictureService.findList(np);
			result.put("pics", nps);
		}
		ri.setData(result);
		return ri;
	}

	@RequestMapping(value = "/noticeDetail/reveiver/score", method = RequestMethod.GET)
	@ApiOperation(value = "通知发布详情查看(接收人查看且是成绩通知)", httpMethod = "GET")
	@ApiImplicitParam(paramType = "query", value = "通知ID", name = "noticeId", required = true, dataType = "long")
	@Transactional
	public ReturnInfo<Map<String, Object>> noticeDetailForReceiverWithScore(long noticeId, HttpServletRequest request) {
		ReturnInfo<Map<String, Object>> ri = new ReturnInfo<>();
		NoticeRange noticeRange = null;
		// 获取查阅状态
		Long currentUserId = getUserId(request);
		Integer userType = getUserType(request);
		if (currentUserId != null && userType != null) {
			NoticeRange nr = new NoticeRange();
			nr.setNoId(noticeId);
			nr.setNrUserId(currentUserId);
			nr.setNrType(userType);
			noticeRange = noticeRangeService.get(nr);
			// 未查阅 则修改查阅状态及时间
			if (noticeRange != null && noticeRange.getNrConsult() == 0) {
				noticeRangeService.consultNotice(noticeId, currentUserId);
			}
		}
		// 通知信息
		Notice noticeInfo = noticeService.getById(noticeId);
		NoticeExtVO nev = new NoticeExtVO();
		try {
			BeanUtils.copyProperties(nev, noticeInfo);
		} catch (Exception e) {
			logger.error("属性拷贝异常");
			e.printStackTrace();
		}
		nev.setNrReceipt(noticeRange.getNrReceipt());
		// 通知发布的发布人名称
		String publisher = getPublisher(noticeInfo.getNoPublisherId());
		//如果是家长身份 则当前用户ID替换为家长对应的学生ID
		if (Constants.PARENT_TYPE.intValue() == userType.intValue()) {
			CommonResponse<List<StudentParentRelVO>> comStudentParentRelList = userService.findStudentParentRelByUpIds(new Long[] {currentUserId});
			List<StudentParentRelVO> studentParentRelList = comStudentParentRelList.getData();
			if (studentParentRelList != null && studentParentRelList.size() > 0) {
				StudentParentRelVO studentParentRel = studentParentRelList.get(0);
				currentUserId = studentParentRel.getUsId();
			}
		}
		// 获取成绩列表及排名
		MyScoreAndRankDto scoreNndRank = iAchievementService.findMyScoreAndRank(noticeInfo.getNoExamId(),currentUserId);
		UserStudent student = studentService.getById(currentUserId);
		Long ciId = student.getCiId();
		String className = getFullClassName(ciId);
		Map<String, Object> result = new HashMap<>();
		result.put("noticeInfo", nev);
		result.put("publisher", publisher);
		result.put("currentUserFeedId", getFeedId(request));
		result.put("publisherFeedId", getFeedId(nev.getNoPublisherId(), 2));
		result.put("className", className);
		result.put("stuName", student.getUsName());
		result.put("scoreList", scoreNndRank.getScoreAndRankMap());
		if (noticeInfo.getNoRanking() == 1) {
			result.put("scoreRank", scoreNndRank.getSort());
		}
		ri.setData(result);
		return ri;
	}

	/**
	 * 获取当前用户ID
	 * 
	 * @param request
	 * @return
	 */
	private Long getUserId(HttpServletRequest request) {
		Long userId = null;
		String feedId = null;
		ToonCode toonCode = (ToonCode) request.getSession().getAttribute(Constants.USER_LOGIN_KEY);
		if (toonCode != null) {
			ToonVisitor visitor = toonCode.getVisitor();
			if (visitor != null) {
				feedId = visitor.getFeed_id();
				CommonResponse<List<TNPToonCard>> jytUserInfo = toonUserService.getJytUserInfo(feedId);
				List<TNPToonCard> data = jytUserInfo.getData();
				if (data != null && data.size() > 0) {
					userId = data.get(0).getCardEntity().getEntityId();
				}
			}
		}
		return userId;
	}

	/*
	 * private Long getUserId(String toonCode) { String tooninfo =
	 * ToonDesUtils.decryptWithSecret(toonCode, appInfoConfig.getAppKey()); String
	 * feedId = null; Long userId = null; ToonCode code =
	 * JSONObject.parseObject(tooninfo, ToonCode.class); if (code != null) {
	 * ToonVisitor visitor = code.getVisitor(); if (visitor != null) { feedId =
	 * visitor.getFeed_id(); CommonResponse<List<TNPToonCard>> jytUserInfo =
	 * toonUserService.getJytUserInfo(feedId); List<TNPToonCard> data =
	 * jytUserInfo.getData(); if (data != null && data.size() > 0) { userId =
	 * data.get(0).getCardEntity().getEntityId(); } } } return userId; }
	 */

	private Long getToonUserId(Long userId, Integer uType) {
		Long toonUserId = null;
		UserType userType = userTypeConvertor(uType);
		CommonResponse<ToonCardEntity> toonCard = userService.getToonCardByUserId(userId, userType);
		ToonCardEntity data = toonCard.getData();
		if (data != null) {
			toonUserId = data.getTceUserId();
		}
		return toonUserId;
	}

	private UserType userTypeConvertor(Integer uType) {
		UserType userType = null;
		// 0 学生 1 家长 2 老师
		if (Constants.STUDENT_TYPE.intValue() == uType.intValue()) {
			userType = UserType.STUDENT;
		} else if (Constants.PARENT_TYPE == uType.intValue()) {
			userType = UserType.PARENT;
		} else {
			userType = UserType.TEACHER;
		}
		return userType;
	}

	private Integer getUserType(HttpServletRequest request) {
		Long userId = null;
		Integer userType = null;
		ToonCode toonCode = (ToonCode) request.getSession().getAttribute(Constants.USER_LOGIN_KEY);
		if (toonCode != null) {
			ToonVisitor visitor = toonCode.getVisitor();
			if (visitor != null) {
				userId = visitor.getUser_id();
				userType = (Integer) request.getSession().getAttribute(String.valueOf(userId));
			}
		}
		return userType;

	}

	private String getFeedId(HttpServletRequest request) {
		String feedId = null;
		ToonCode toonCode = (ToonCode) request.getSession().getAttribute(Constants.USER_LOGIN_KEY);
		if (toonCode != null) {
			ToonVisitor visitor = toonCode.getVisitor();
			if (visitor != null) {
				feedId = visitor.getFeed_id();
			}
		}
		return feedId;
	}

	/*
	 * @RequestMapping(value = "/noticeInfo/reveiver", method = RequestMethod.GET)
	 * 
	 * @ApiOperation(value = "通知发布概览列表查看(接收人查看)", httpMethod = "GET")
	 * 
	 * @ApiImplicitParam(paramType = "query", value = "分页页数", name =
	 * "pageNumber",required = true, dataType = "int"),
	 * 
	 * @ApiImplicitParam(paramType = "query", value = "分页大小", name =
	 * "pageSize",required = true, dataType = "int") }) public
	 * ReturnInfo<List<Notice>> getNoticeForReceiver(long currenUserId,int
	 * pageNumber,int pageSize) { ReturnInfo<List<Notice>> ri = new ReturnInfo<>();
	 * List<Notice> list =noticeService.getNoticeForReceiver(currenUserId,
	 * pageNumber, pageSize); for(Notice notice : list) { if (notice.getNoType() ==
	 * Constants.NOTICE_HOMEWORK){ String dateInfo =
	 * DateUtils.date2String(notice.getCreateTime(),DateUtils.DATE_FORMAT_DATEONLY);
	 * String weekInfo = DateUtils.dateToWeek(dateInfo); String title = dateInfo +
	 * " " + weekInfo + notice.getNoSubject() +"做业"; notice.setNoTitle(title); }else
	 * if(notice.getNoType() == Constants.NOTICE_SCORE) { notice.setNoTitle("考试成绩");
	 * } } ri.setData(list); return ri; }
	 */

	private int getPersonCount(long noticeId, int queryType, int param) {
		// 获取人员数量
		NoticeRange noticeRange = new NoticeRange();
		noticeRange.setNoId(noticeId);
		// 1 需回执 0 不需要回执
		if (queryType == 1) {
			noticeRange.setNrReceipt(param);
		} else {
			noticeRange.setNrConsult(param);
		}
		int count = noticeRangeService.getCount(noticeRange);
		return count;
	}

	private String getClassName(long noticeId) {
		// 通知发布班级名称
		StringBuilder className = new StringBuilder();
		List<Long> classIds = noticeRangeService.getClassByNoticeId(noticeId);
		for (int i = 0, len = classIds.size(); i < len; i++) {
			if (i >= 2) {
				className.delete(className.length() - 1, className.length());
				className.append("等");
				break;
			}
			String fullClassName = getFullClassName(classIds.get(i));
			className.append(fullClassName).append("、");
		}
		if (className.toString().endsWith("、")) {
			className.delete(className.length() - 1, className.length());
		}
		return className.toString();
	}

	private String getFullClassName(Long ciId) {
		ClassInfo classInfo = new ClassInfo();
		classInfo.setCiId(ciId);
		ClassInfo classInfoResult = classService.get(classInfo);
		CommonResponse<Grade> cr = organizationService.findGradeById(classInfoResult.getGrId());
		String className = classInfoResult.getCiName();
		String gradeName = cr.getData().getGrName();
		return gradeName + className;
	}

	// 获取发布人姓名
	private String getPublisher(long utId) {
		UserTeacher ut = new UserTeacher();
		ut.setUtId(utId);
		CommonResponse<UserTeacher> userTeacher = userService.getUserTeacher(ut);
		String utName = userTeacher.getData().getUtName();
		return utName;
	}

	private void publishByToon(Notice notice, NoticeRange noticeRange, HttpServletRequest request) {
		Long userId = noticeRange.getNrUserId();
		Integer userType = noticeRange.getNrType();
		// 调用toon发消息
		UserBean userBean = new UserBean();
		userBean.setNoId(notice.getNoId());
		userBean.setTitle(getTitleByNoticeType(notice));
		userBean.setContent(getContent(notice));
		userBean.setFromToonFeed(getFeedId(request));
		userBean.setToToonFeed(getFeedId(userId, userType));
		userBean.setToonUserId(getToonUserId(userId, userType));
		StringBuilder url = new StringBuilder();
		url.append(request.getScheme()).append("://");
		url.append(request.getServerName()).append(":");
		url.append(request.getServerPort()).append(request.getContextPath());
		UserType uType = userTypeConvertor(noticeRange.getNrType());
		Map<String, Object> map = new HashMap<>();
		map.put("userType", uType.getUserTypeCode());
		String jsonString = JSON.toJSONString(map);
		url.append("/html/index.html?param=").append(jsonString);
		if (notice.getNoType() == Constants.NOTICE_SCORE) {
			url.append("&path=scoreDetail&noId=").append(notice.getNoId());
		} else {
			url.append("&path=receiveDetails&noId=").append(notice.getNoId());
		}
		url.append("&from=notify&toongine=101&noType=").append(notice.getNoType());
		logger.info("查看详情url=========" + url.toString());
		userBean.setUrl(url.toString());
		yearController.sendToonNotice(userBean);
	}

	// 获取用户feedId
	private String getFeedId(Long userId, int type) {
		UserType userType = userTypeConvertor(type);
		CommonResponse<ToonCardEntity> toonCard = userService.getToonCardByUserId(userId, userType);
		String feedId = null;
		ToonCardEntity entity = toonCard.getData();
		if (entity != null) {
			feedId = entity.getFeedId();
		}
		return feedId;
	}

	// 通知类型如果是成绩需组装通知内容
	private String getContent(Notice notice) {
		String content = "";
		int type = notice.getNoType();
		if (notice.getNoPicture() == 1) {
			content = Constants.PICTURE;
		}
		if (type == Constants.NOTICE_SCORE) {
			content += notice.getNoContent() + Constants.EXAM_SCORE;
		} else {
			content += notice.getNoContent();
		}
		return content;
	}

	// 根据通知类型组装通知title
	private String getTitleByNoticeType(Notice notice) {
		String title = null;
		int type = notice.getNoType();
		if (type == Constants.NOTICE_HOMEWORK) {
			String date = getDateAndWeek(new Date());
			title = date + " " + notice.getNoTitle();
		} else if (type == Constants.NOTICE_SCORE) {
			title = Constants.EXAM_SCORE;
		} else {
			title = notice.getNoTitle();
		}
		return title;
	}

	// 获取日期 格式为"2018-05-03 星期四"
	private String getDateAndWeek(Date date) {
		String dateInfo = DateUtils.date2String(date, DateUtils.DATE_FORMAT_DATEONLY);
		String weekInfo = DateUtils.dateToWeek(dateInfo);
		String result = dateInfo + " " + weekInfo;
		return result;
	}
}
