<template>
	<view class="app_window im">
		<view class="im_head">
			<text class="page_name">消息</text>
			<view class="more_add" @click="addGroup">
				<text class="iconfont icon-40 more_icon"></text>
			</view>
		</view>
		<view class="search_box">
			<input class="search_ipt" type="text" placeholder="请输入关键字" @input="handleInput" />
		</view>
		<scroll-view scroll-y="true" class="im_scroll" :style="'height:'+Number(appInfo.appHeight-100)+'px;'">
			<view class="im_list">
				<view class="im_item" v-for="item in chatList" :key="item.id"
					@click="openChat(item.groupId,item.userName,item.unReadCount,item.userType,item.groupType)">
					<view class="im_left">
						<!-- 有头像 -->
						<image :src="item.avatar" mode="aspectFill" class="im_icon" v-if="item.avatar!=''"></image>
						<!-- 无头像 -->
						<view class="im_icon icon_bg" v-else>
							<!-- 是数字 -->
							<text class="icon_number" v-if="!isNaN(parseInt(item.userName))">{{item.userName.slice(0,4)}}</text>
							<block v-else>
								<!-- 是中文 -->
								<text class="icon_chinese" v-if="/^[\u4e00-\u9fa5]+$/.test(item.userName)">{{item.userName.slice(0,2)}}</text>
								<!-- 是字符串 -->
								<text class="icon_string" v-else>{{item.userName.slice(0,2)}}</text>
							</block>
						</view>
						<view class="menu_hint" v-if="item.unReadCount>0">
							<text v-if="item.unReadCount<=99">{{item.unReadCount>99?'99':item.unReadCount}}</text>
							<u-icon v-else class="hint_icon" name="more-dot-fill" color="#FFF" size="26"></u-icon>
						</view>
					</view>
					<view class="im_right">
						<view class="im_top">
							<view class="top_head">
								<text v-if="item.onlineStatus==1" class="im_status im_on"></text>
								<text v-if="item.onlineStatus==0" class="im_status im_off"></text>
								<text class="im_name">{{item.userName}}</text>
							</view>
							<text class="im_date">{{tool.getDetailDate(item.loginDate)}}</text>
						</view>
						<rich-text class="im_msg" :nodes="item.lastMessage"></rich-text>
					</view>
				</view>
			</view>
		</scroll-view>
		<drag-button v-if="top" :isDock="true" text='恢复' @btnClick="topClick" />
	</view>
</template>

<script>
	import tool from '@/utils/tools.js'
	export default {
		data() {
			return {
				appInfo: '',
				top: false,
				callType: 'call',
				chatList:[],
				timer1:null,
				searchValue:"",
			}
		},
		onLoad() {
			this.appInfo = uni.getStorageSync('appInfo');
			this.loadChatList();
			this.getListTimer();
			uni.$on('window', data => {
				if (data.msg == "videoRecover") {
					this.callType = 'video';
					this.top = true;
				} else if (data.msg == "callRecover") {
					this.callType = 'call';
					this.top = true;
				} else {
					this.top = false;
				}
			});
			
			//监听修改群聊名称消息
			uni.$on('editGroupName3',data=>{
				this.loadChatList();
			});
			
			//解散群聊监听
			uni.$on('disbandGroup',data=>{
				this.loadChatList();
			});
			
			
			//群聊新建监听
			uni.$on('addGroup', (data) => {
				this.loadChatList();
				});


			uni.$on('chatListChatList', (data) => {
				console.log("监听到事件：chatListChatList ，携带参数 msg 为:" + JSON.stringify(data));
				//收到非本人发出的消息
				if (data.msg == 'resTrimMessage') {
					for (var i = 0; i < this.chatList.length; i++) {
						//修改对应用户的最后消息
						//群消息
						if (data.value.message.type == 1) {
							if (this.chatList[i].groupId == data.value.message.chatId && data.value.message
								.fromId != uni.getStorageSync("LoginUser").userId) {
								console.log("sendTime:" + this.chatList[i].loginDate + "获取时间：" + new Date()
									.getTime())
								this.chatList[i].loginDate = new Date().getTime();

								if (data.value.message.messageType == 0) {
									this.chatList[i].lastMessage = data.value.message.content.replace(
										/\/img\/emote/g, "/static/img/emote");
								} else if (data.value.message.messageType == 1) {
									this.chatList[i].lastMessage = "[图片]";
								} else if (data.value.message.messageType == 2) {
									this.chatList[i].lastMessage = "[音频]";
								} else if (data.value.message.messageType == 3) {
									this.chatList[i].lastMessage = "[语音]";
								} else if (data.value.message.messageType == 4) {
									this.chatList[i].lastMessage = "[视频]";
								} else if (data.value.message.messageType == 5) {
									this.chatList[i].lastMessage = "[文件]";
								}else if (data.value.message.messageType == 6) {
									this.chatList[i].lastMessage = "[定位]";
								}
								this.chatList[i].unReadCount = this.chatList[i].unReadCount + 1;
								this.moveItemToFirst(this.chatList[i].groupId);
								break;

							}
						}
						//私聊
						else {
							if (this.chatList[i].groupId == data.value.message.fromId) {
								this.chatList[i].loginDate = new Date().getTime();
								if (data.value.message.messageType == 0) {
									this.chatList[i].lastMessage = data.value.message.content.replace(
										/\/img\/emote/g, "/static/img/emote");
								} else if (data.value.message.messageType == 1) {
									this.chatList[i].lastMessage = "[图片]";
								} else if (data.value.message.messageType == 2) {
									this.chatList[i].lastMessage = "[音频]";
								} else if (data.value.message.messageType == 3) {
									this.chatList[i].lastMessage = "[语音]";
								} else if (data.value.message.messageType == 4) {
									this.chatList[i].lastMessage = "[视频]";
								} else if (data.value.message.messageType == 5) {
									this.chatList[i].lastMessage = "[文件]";
								}
								else if (data.value.message.messageType == 6) {
									this.chatList[i].lastMessage = "[定位]";
								}
								this.chatList[i].unReadCount = this.chatList[i].unReadCount + 1;
								this.moveItemToFirst(this.chatList[i].groupId);
								break;

							}
						}
					}

				}
			});

			uni.$on('chatLastMessage', (data) => {
				console.log("监听到事件：chatLastMessage ，携带参数 msg 为:" + JSON.stringify(data));
				//收到自己收到的最后一条消息
				if (data.msg == 'lastMessage') {
					for (var i = 0; i < this.chatList.length; i++) {
						if (data.value.userId == this.chatList[i].groupId) {
							this.chatList[i].loginDate = data.value.sendTime;
							if (data.value.messageType == 0) {
								this.chatList[i].lastMessage = data.value.content;
							} else if (data.value.messageType == 1) {
								this.chatList[i].lastMessage = "[图片]";
							} else if (data.value.messageType == 2) {
								this.chatList[i].lastMessage = "[音频]";
							} else if (data.value.messageType == 3) {
								this.chatList[i].lastMessage = "[语音]";
							} else if (data.value.messageType == 4) {
								this.chatList[i].lastMessage = "[视频]";
							} else if (data.value.messageType == 5) {
								this.chatList[i].lastMessage = "[文件]";
							}
							else if (data.value.messageType == 6) {
								this.chatList[i].lastMessage = "[定位]";
							}
							this.moveItemToFirst(this.chatList[i].groupId);
							break;

						}

					}
				}
			});


			uni.$on('fromIdUnreadChatList', (data) => {
				console.log("监听到事件：fromIdUnreadChatList ，携带参数 msg 为:" + JSON.stringify(data));
				if (data.msg == 'from') {
					for (var i = 0; i < this.chatList.length; i++) {
						if (data.value == this.chatList[i].groupId) {
							this.chatList[i].unReadCount = 0;
							break;
						}
					}
				}
			});
			let topObj = uni.getStorageSync('topObj') || '';
			if (topObj == "videoRecover") {
				this.callType = 'video';
				this.top = true;
			} else if (topObj == "callRecover") {
				this.callType = 'call';
				this.top = true;
			} else {
				this.top = false;
			}
		},
		onUnload() {
			console.log("chatList监听移除!");
			// 移除监听事件
			uni.$off('chatListChatList');
			uni.$off('chatLastMessage');
			uni.$off('fromIdUnreadChatList');
			uni.$off('addGroup');
			uni.$off('editGroupName3');
			uni.$off('disbandGroup');
			
			
			clearInterval(this.timer1); // 清除定时器
		},
		methods: {
			topClick() {
				this.top = false;
				if (this.callType == 'video') {
					uni.navigateTo({
						url: '/pages/video/video'
					});
				} else if (this.callType == 'call') {
					uni.navigateTo({
						url: '/pages/calling/calling'
					});
				}
			},
			addGroup(){
					uni.navigateTo({
						url:'/pages/groupAdd/groupAdd'
					})
			},
			loadChatList(){
				var params = {
					appId: uni.getStorageSync("LoginUser").userId,
					keyWord: this.searchValue,
				};
				this.api.post("/app/trim/userList", params).then(res => {
					this.chatList=res.data;
					 for(var i=0;i<this.chatList.length;i++){
						 if(this.chatList[i].avatar!=""){
						 this.chatList[i].avatar="http://"+uni.getStorageSync("serverIp")+this.chatList[i].avatar;
						 }
						 if(this.chatList[i].onlineStatus==1){
							 this.chatList[i].userName=this.chatList[i].userName;
						 }
						 else{
							 this.chatList[i].userName=this.chatList[i].userName;
						 }
						 }
						// console.log(this.chatList);
						 // console.log("获取userList:"+JSON.stringify(this.chatList));
				}) 
			},
			//模糊搜索
			handleInput(event){
				console.log("获取！"+event.target.value);
				this.searchValue=event.target.value;
				this.loadChatList();
			},
			 moveItemToFirst(key) {
			      const index = this.chatList.findIndex(item => item.groupId === key);
			      if (index !== -1) {
			        const item = this.chatList.splice(index, 1)[0]; // 移除指定key值的数据
			        this.chatList.unshift(item); // 将指定数据移到第一条
			      }
			    },
				//定时获取列表信息
				getListTimer(){
					this.timer1 = setInterval(() => {
						console.log("定时获取消息列表！")
					this.loadChatList();
					}, 10000);
				},
			openChat(groupId,userName,unReadCount,userType,groupType){
				console.log("获取到用户列表："+JSON.stringify(this.chatList))
	            for(var i=0;i<this.chatList.length;i++){
					//修改当前列表的未读值
					if (this.chatList[i].groupId == groupId) {
						this.chatList[i].unReadCount = 0;
						break;
					}
				}
				uni.navigateTo({
					url: '/pages/chatDetail/chatDetail?userId=' + groupId + '&userName=' + userName +
						'&userType=' + userType+'&groupType='+groupType
				})




				//修改index页面即时通讯的未读数量,大于0时处理，并给中间件发送一个已查看消息
				if (unReadCount > 0) {
					//修改index页面即时通讯的未读数量
					uni.$emit('updateChatUnReadCount', {
						msg: "updateCount",
						value: unReadCount
					})

					var params = {
						toId: uni.getStorageSync("LoginUser").userId,
						fromId: groupId,
						userType: userType
					};
					//修改数据库的值
					this.api.post("/app/trim/clearUnReadCount", params);



					//给中间件发送一个已查看消息
					let msgRead = { //普通对话消息
						code: "3",
						message: {
							chatId: uni.getStorageSync("LoginUser").userId, //用户ID
							userId: uni.getStorageSync("LoginUser").userId, //用户ID
							timestamp: new Date().getTime(),
							type: 0
						}
					};

					let msgGroupRead = { //群聊消息
						code: "3",
						message: {
							chatId: groupId, //群ID
							userId: uni.getStorageSync("LoginUser").userId, //用户ID
							timestamp: new Date().getTime(),
							type: "1"
						}
					};
					//判断私聊消息还是群消息
					if (userType == "00") {
						console.log("获取已阅读私聊消息：" + JSON.stringify(msgRead));
						this.wsIM.send(JSON.stringify(msgRead));
					} else {
						console.log("获取已阅读群消息：" + JSON.stringify(msgGroupRead));
						this.wsIM.send(JSON.stringify(msgGroupRead));
					}
				}
			}
		}
	}
</script>

<style lang="scss" scoped>
	.im {
		display: flex;
		flex-direction: column;
		position: relative;
		.im_head {
			height: 60px;
			display: flex;
			justify-content: center;
			align-items: center;
			padding: 0 40rpx;
			position: relative;
			flex: none;
			background-color: #EDEDF7;
			border-bottom: 2rpx solid #ddd;
			.more_add {
				position: absolute;
				top: 0;
				right: 0;
				height: 60px;
				padding: 0 40rpx;
				display: flex;
				align-items: center;
				.more_icon {
					font-size: 60rpx;
				}
			}
			.more_add:active {
				.more_icon {
					color: #3c9cff;
				}
			}
		}
		.search_box{
			padding: 20rpx 30rpx;
			border-bottom: 2rpx solid #e5e5e5;
			.search_ipt{
				background-color: #EDEDF7;
				height: 40px;
				padding: 0 30rpx;
				font-size: 32rpx;
			}
			
		}
		.im_scroll {
			flex: auto;
			height: 100%;
			overflow: hidden auto;

			.im_list {
				.im_item {
					display: flex;
					padding: 30rpx 30rpx;
					border-bottom: 2rpx solid #e5e5e5;

					.im_left {
						position: relative;
						width: 100rpx;
						height: 100rpx;
						flex: none;

						.im_icon {
							width: 100%;
							height: 100%;
							border-radius: 20rpx;
						}
						.icon_bg {
							background-color: #3c9cff;
							color: #fff;
							display: flex;
							align-items: center;
							justify-content: center;
						}
						.icon_number{font-size: 34rpx;}
						.icon_chinese{font-size: 34rpx;}
						.icon_string{font-size: 40rpx;}

						.menu_hint {
							position: absolute;
							top: -10rpx;
							right: -10rpx;
							background-color: #FA4B5A;
							width: 50rpx;
							height: 50rpx;
							;
							border-radius: 50%;
							color: #fff;
							font-size: 30rpx;
							display: flex;
							justify-content: center;
							align-items: center;

							.hint_icon {}
						}
					}

					.im_right {
						flex: auto;
						height: 100rpx;
						padding-left: 30rpx;
						overflow: hidden;
						display: flex;
						flex-direction: column;
						justify-content: center;

						.im_top {
							display: flex;
							align-items: center;
							justify-content: space-between;
							margin-bottom: 6rpx;
							.top_head{
								display: flex;
								align-items: center;
								.im_name {
									font-size: 32rpx;
									white-space: nowrap;
									overflow: hidden;
									text-overflow: ellipsis;
								}
								.im_status{
									width: 24rpx;
									height: 24rpx;
									border-radius: 50%;
									margin-right:10rpx;
								}
								.im_on{
									background-color: #03D352;
								}
								.im_off{
									background-color: #999;
								}
							}
							

							.im_date {
								color: #999;
								font-size: 26rpx;
							}
						}

						.im_msg {
							color: #999;
							font-size: 26rpx;
							white-space: nowrap;
							overflow: hidden;
							text-overflow: ellipsis;
						}
					}

				}

				.im_item:active {
					background-color: #eee;
				}
			}
		}
	}
</style>