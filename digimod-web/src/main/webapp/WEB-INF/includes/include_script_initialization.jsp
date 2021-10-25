		<script type="text/javascript">
			// BASIC FUNCTION	
			
			function PreferenceInitialization(UserPreference, DefaultPreference)
			{
				if(UserPreference.length > 1)
				{
					CurrentPreference = UserPreference;
				}
				else
				{
					CurrentPreference = DefaultPreference;
				}
				
				return CurrentPreference;
			}
			
			
			// INITIALIZATION
			
			// THEME
			
			var NavigationCounter = 0;
			var UserTheme = '<s:property value="%{#session.LOGIN_KEY.userPreference.theme}"/>';
			var CurrentTheme = PreferenceInitialization(UserTheme, '<s:property value="@id.co.emobile.samba.web.data.WebConstant@DEFAULT_THEME"/>');
			var ThemeArray;
			var ThemeCSSArray;
			
			// FONT SIZE
			
			var UserFontSize = '<s:property value="%{#session.LOGIN_KEY.userPreference.fontSize}"/>';
			var CurrentFontSize = PreferenceInitialization(UserFontSize, '<s:property value="@id.co.emobile.samba.web.data.WebConstant@DEFAULT_FONTSIZE"/>');
			var FontSizeArray;
			var FontSizeCSSArray;
			
			// FONT FAMILY
			
			var UserFontFamily = '<s:property value="%{#session.LOGIN_KEY.userPreference.fontFamily}"/>';
			var CurrentFontFamily = PreferenceInitialization(UserFontFamily, '<s:property value="@id.co.emobile.samba.web.data.WebConstant@DEFAULT_FONTFAMILY"/>');
			var FontFamilyArray;
			var FontFamilyCSSArray;
			
			// LAYOUT ID
			
			var MessageResultID = "#MessageResult";
			var MessageDefault = "MessageDefault";
			var MessageState = "MessageState";
			var ResultSuccess = "ResultSuccess";
			var ResultFailure = "ResultFailure";
			var LoadingSquareClass = "#MessageLoading";
			
			// TIME STAMP
			
			Date.now = Date.now || function() { return +new Date; }; 
			var ProcessStart;
			var ProcessEnd;
			var ProcessTime;
			
			// AJAX
			
			var formParam;
			var resultObject;
			var resultSearch;
			var firstURL;
			var prevURL;
			var nextURL;
			var lastURL;
			var combinedPanel;
			var isValid = null;
			var messageSubject;
			var messageSuccess;
			var currentPage;
			var sortVariable;
			
			
			// FUNCTION
			
			function RefreshGreeting(ContainerGreeting)
			{
				var RAWDateTime = new Date();
				var ResultHour = RAWDateTime.getHours();
				var ResultGreeting;
				
				if(ResultHour >= 4 && ResultHour <	10)
				{
					// resultGreeting = "Good morning";
					ResultGreeting = '<s:text name="w.goodMorning"/>';
				}
				else if(ResultHour >= 10 && ResultHour < 13)
				{
					// resultGreeting = "Good day";
					ResultGreeting = '<s:text name="w.goodDay"/>';
				}
				else if(ResultHour >= 13 && ResultHour < 17)
				{
					// resultGreeting = "Good afternoon";
					ResultGreeting = '<s:text name="w.goodAfternoon"/>';
				}
				else if(ResultHour >= 17 && ResultHour < 21)
				{
					// resultGreeting = "Good evening";
					ResultGreeting = '<s:text name="w.goodEvening"/>';
				}
				else if((ResultHour >= 21 && ResultHour < 24) || (ResultHour >= 0 && ResultHour < 4))
				{
					// resultGreeting = "Good night";
					ResultGreeting = '<s:text name="w.goodNight"/>';
				}
				else
				{
					// resultGreeting = "Hello !";
					ResultGreeting = '<s:text name="w.hello"/>';
				}
				
				$(ContainerGreeting).text(ResultGreeting);
				tt=setTimeout('refreshGreeting("'+ContainerGreeting+'")', 3600000);
			}
			
			function MessageResult(message, messageResultID)
			{
				if(message.length > 1)
				{
					$(messageResultID).removeClass(MessageState);
					$(messageResultID).addClass(ResultSuccess);
					$(messageResultID).empty();
					$(messageResultID).append(message);
				}
				else
				{
					$(messageResultID).removeClass();
					$(messageResultID).addClass(MessageDefault);
					$(messageResultID).addClass(MessageState);
					$(messageResultID).empty();
				}
			}
			
			function MessageFail(message, messageResultID)
			{
				if(message.length > 1)
				{
					$(messageResultID).removeClass(MessageState);
					$(messageResultID).addClass(ResultFailure);
					$(messageResultID).empty();
					$(messageResultID).append(message);
				}
				else
				{
					$(messageResultID).removeClass();
					$(messageResultID).addClass(MessageDefault);
					$(messageResultID).addClass(MessageState);
					$(messageResultID).empty();
				}
			}
			
			function ButtonRequest(FormID, ButtonID, ProgressRequest, MessageResultID, URL)
			{
				$(ButtonID).click(function()
				{
					$(MessageResultID).removeClass(MessageState);					
					ProcessStart = Date.now();
					$(ProgressRequest).show();
					formParam = $(FormID).serialize();
					
					// PROCESS
					
					$.ajax
					({
						type 		: 'POST', // define the type of HTTP verb we want to use (POST for our form)
						url 		: /* '<s:property value="%{remoteurl}" />' */ URL, // the url where we want to POST
						data 		: formParam, // our data object
					}).done(function(resultJson) 
					{
						if(resultJson.substr(2,7)=="DOCTYPE")
						{
							window.location.href='<s:property value="@id.co.emobile.samba.web.data.WebConstant@LOGIN_PATH"/>';
						}
						$(ProgressRequest).hide();
						resultObject = JSON.parse(resultJson);
						
						if(resultObject.rc == 0)
						{
							if(resultObject.type > 0)
							{
								window.location.href=resultObject.path;	
							}
							else
							{
								$(FormID).trigger("reset");
								ProcessEnd = Date.now();
								ProcessTime = (ProcessEnd - ProcessStart) / 1000;
								
								$(MessageResultID).addClass(ResultSuccess);
								$(MessageResultID).empty();
								$(MessageResultID).append(resultObject.message);
							}
						}
						else
						{
							$(MessageResultID).addClass(ResultFailure);
							$(MessageResultID).empty();
							$(MessageResultID).append(resultObject.message);
							window.location.hash = '#form1';
						}
					});
					
					// TIMEOUT
					
					setTimeout(function()
					{
						$(ProgressRequest).hide();
					}, 
					15000);
					
					setTimeout(function()
					{
						$(LoadingSquareClass).append("Process took longer than usual, press ESC to cancel.");
						
						$(document).keyup(function(e) 
						{
							if (e.keyCode == 27)
							{
								$(ProgressRequest).hide();
							}
							else
							{
								
							}
						});
					}, 
					5000);
					
					return false;
				});
			}
			
			
			function ButtonRequestAdditionalFunction(FormID, ButtonID, ProgressRequest, MessageResultID, URL)
			{
				$(ButtonID).click(function()
				{
					$(MessageResultID).removeClass(MessageState);					
					ProcessStart = Date.now();
					$(ProgressRequest).show();
					formParam = $(FormID).serialize();
					
					// PROCESS
					
					$.ajax
					({
						type 		: 'POST', // define the type of HTTP verb we want to use (POST for our form)
						url 		: /* '<s:property value="%{remoteurl}" />' */ URL, // the url where we want to POST
						data 		: formParam, // our data object
					}).done(function(resultJson) 
					{
						if(resultJson.substr(2,7)=="DOCTYPE")
						{
							window.location.href='<s:property value="@id.co.emobile.samba.web.data.WebConstants@LOGIN_PATH"/>';
						}
						$(ProgressRequest).hide();
						resultObject = JSON.parse(resultJson);
						
						if(resultObject.rc == 0)
						{
							if(resultObject.type > 0)
							{
								window.location.href=resultObject.path;	
							}
							else
							{
								$(FormID).trigger("reset");
								ProcessEnd = Date.now();
								ProcessTime = (ProcessEnd - ProcessStart) / 1000;
								
								$(MessageResultID).addClass(ResultSuccess);
								$(MessageResultID).empty();
								//$(MessageResultID).append(resultObject.message + ", process took " + ProcessTime + " seconds");
								$(MessageResultID).append(resultObject.message);
							}
						}
						else
						{
							$(MessageResultID).addClass(ResultFailure);
							$(MessageResultID).empty();
							$(MessageResultID).append(resultObject.message);
							window.location.hash = '#form1';
						}
						additionalFunction();
					});
					
					// TIMEOUT
					
					setTimeout(function()
					{
						$(ProgressRequest).hide();
					}, 
					15000);
					
					setTimeout(function()
					{
						$(LoadingSquareClass).append("Process took longer than usual, press ESC to cancel.");
						
						$(document).keyup(function(e) 
						{
							if (e.keyCode == 27)
							{
								$(ProgressRequest).hide();
							}
							else
							{
								
							}
						});
					}, 
					5000);
					
					return false;
				});
			}
			
			function ButtonAuthorize(FormID, ButtonClass, ProgressRequest, MessageResultID, URL)
			{
				$(ButtonClass).click(function()
				{				
					if($(this).attr('id')=='btnApprove')
					{
						$('#authStatus').val('<s:property value="@id.co.emobile.samba.web.data.WebConstants@AUTH_STATUS_APPROVE" />');	
					}
					else
					{
						$('#authStatus').val('<s:property value="@id.co.emobile.samba.web.data.WebConstants@AUTH_STATUS_REJECT" />');
					}
					
					$(MessageResultID).removeClass(MessageState);
					ProcessStart = Date.now();
					$(ProgressRequest).show();
					formParam = $(FormID).serialize();
					
					$.ajax
					({
						type 		: 'POST', // define the type of HTTP verb we want to use (POST for our form)
						url 		: /* '<s:property value="%{remoteurl}" />' */ URL, // the url where we want to POST
						data 		: formParam, // our data object
					}).done(function(resultJson) 
					{
						if(resultJson.substr(2,7)=="DOCTYPE")
						{
							window.location.href='UssdParamData';
						}
						$(ProgressRequest).hide();
						resultObject = JSON.parse(resultJson);
						
						if(resultObject.rc == 0)
						{
							if(resultObject.type > 0)
							{
								window.location.href=resultObject.path;	
							}
							else
							{
								$(FormID).trigger("reset");
								ProcessEnd = Date.now();
								ProcessTime = (ProcessEnd - ProcessStart) / 1000;
								
								$(MessageResultID).addClass(ResultSuccess);
								$(MessageResultID).empty();
								$(MessageResultID).append(resultObject.message);
							}
						}
						else
						{
							$(MessageResultID).addClass(ResultFailure);
							$(MessageResultID).empty();
							$(MessageResultID).append(resultObject.message);
						}
					});
					
					// TIMEOUT
					
					setTimeout(function()
					{
						$(ProgressRequest).hide();
					}, 
					15000);
					
					setTimeout(function()
					{
						$(LoadingSquareClass).append("Process took longer than usual, press ESC to cancel.");
						
						$(document).keyup(function(e) 
						{
							if (e.keyCode == 27)
							{
								$(ProgressRequest).hide();
							}
							else
							{
								
							}
						});
					}, 
					5000);
					
					return false;
				});
			}	
			
			function isNumberKey(evt)
			{
				var charCode = (evt.which) ? evt.which : event.keyCode
				if (charCode > 31 && (charCode < 48 || charCode > 57))
					return false;
				return true;
			}
			
			function isNumberOnly(evt)
			{
				var charCode = (evt.which) ? evt.which : event.keyCode
				if ((charCode > 47 && charCode < 58 ) || charCode == 8 )
					return true;
				return false;
			}
			
			function spaceNotAllowed(evt)
			{
				var charCode = (evt.which) ? evt.which : event.keyCode
				if (charCode == 32)
					return false;
				return true;
			}
			
			function fileNameForbiddenChar(evt)
			{
				var charCode = (evt.which) ? evt.which : event.keyCode
				if (charCode == 47 || charCode == 92 || charCode == 63 || charCode == 58 || 
						charCode == 42 || charCode == 34 || charCode == 60 || charCode == 62 ||
						charCode == 124)
					return false;
				return true;
			}
			
			function isFloatFormat(evt)
			{
				var charCode = (evt.which) ? evt.which : event.keyCode
				if ((charCode > 47 && charCode < 58 ) || charCode == 8 || charCode == 46 )
					return true;
				return false;
			}
			
        </script>