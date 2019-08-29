package com.solace.myannotation.enums;

public class MqConfigEnum {

    /**
     * 作者 CG
     * 时间 2019/4/18 11:03
     * 注释 MQ key
     */
    public enum Mq {
        /**
         * 作者 CG
         * 时间 2019/8/8 14:09
         * 注释 微喵订座
         */
        RESERVATION("TOP-TIMER","tagSpellSong","reservation"),
         /**
           * 作者 CG
           * 时间 2019/5/9 17:03
           * 注释 艺人自动上下台
           */
        ARTISTCONFIG("TOP-TIMER","tagSpellSong","artistConfig"),
         /**
           * 作者 LH
           * 时间 2019/5/16 10:10
           * 注释 砸金蛋
           */
        SMASHEGG("TOP-TIMER","tagSpellSong","smashegg"),
         /**
           * 作者 CG
           * 时间 2019/5/9 17:04
           * 注释 拼歌
           */
        SPELLSONG("TOP-TIMER","tagSpellSong","ssGroupInfo"),
        /**
         * 作者 LH
         * 时间 2019/7/3
         * 注释 砸金蛋即买即开
         */
        SMASHEGGCURRENT("TOP-TIMER","tagSpellSong","smasheggcurrent"),
        /**
         * 作者 CSH
         * 时间 2019/7/3
         * 注释 操作者提现订单跟踪任务
         */
        OPERATOR_TRANSFER("TOP-TIMER","tagSpellSong","operator_transfer"),

        /**
         * 作者 LJX
         * 时间 2019/8/14
         * 注释 喵Star延迟推送微信模板消息
         */
        MINISTART_PUSHTEMPLATE_MSG("TOP-TIMER","tagSpellSong","miniStarPushSong");
        private String topic;
        private String tags;
        private String key;

        Mq(String topic, String tags, String key) {
            this.topic = topic;
            this.tags = tags;
            this.key = key;
        }

        public String getTopic() {
            return topic;
        }

        public void setTopic(String topic) {
            this.topic = topic;
        }

        public String getTags() {
            return tags;
        }

        public void setTags(String tags) {
            this.tags = tags;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }
    }
}
