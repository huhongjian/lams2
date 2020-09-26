package com.bupt.lams.constants;

/**
 * 工作流常量
 */
public class WorkflowConstant {
    /**
     * 代理商验证任务名称
     */
    public static String WORKFLOW_AGENT_VALIDATE_RECEIVE_TASK = "agentValidateRecTask";
    /**
     * 操作类型
     */
    public static String OPERATE_TYPE = "operateType";
    /**
     * 指定的受理人1
     * 多个节点的工作流中第一个节点使用：WORKFLOW_PARAM_KEY_FIRST_ASSIGNEE
     */
    public static String WORKFLOW_PARAM_KEY_FIRST_ASSIGNEE = "firstAssignee";
    /**
     * 回复上一受理人
     * 三个及以上节点的工作流中第二个节点使用：WORKFLOW_PARAM_KEY_RESPONSE_TO_ASSIGNEE，中间节点不支持回复到具体人
     * 只有OpManagerWithTranferProcess在使用
     */
    public static String WORKFLOW_PARAM_KEY_RESPONSE_TO_ASSIGNEE = "responseToAssignee";
    /**
     * 上一级转交人
     */
    public static String WORKFLOW_PARAM_KEY_LAST_PERCESS_PERSON = "lastPercessPerson";
    /**
     * 受理人2，转交组
     */
    public static String WORKFLOW_PARAM_KEY_CANDIDATE_GROUPS = "candidateGroups";
    /**
     * 受理人2，转交人
     */
    public static String WORKFLOW_PARAM_KEY_CANDIDATE_USERS = "candidateUsers";
    /**
     * 受理人1上约定的转交组/人参数
     */
    public static String WORKFLOW_PARAM_KEY_CANDIDATE_GROUPS_USERS = "candidateGroupsAndUsers";
    /**
     * 任务节点对应的上个节点处理人映射关系
     */
    public static String WORKFLOW_PARAM_KEY_TASKNAME_MAP_REPLYUSER = "taskNameMapReplyUser";
    /**
     * 下一个处理人
     */
    public static String NEXT_USER = "nextUser";
}
