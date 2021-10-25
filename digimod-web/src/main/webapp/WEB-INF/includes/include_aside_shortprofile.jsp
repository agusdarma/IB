			<s:url id="home" action="MainMenu" includeParams="none"/>
			<s:a href="%{home}" cssClass="ClearHyperlink">
               	<input type="button" id="ButtonHome" value=""/>
            </s:a>
               	
			<aside class="ProfileShort">
				<span id="ContainerGreeting"></span>, 
				<mark><s:property value="%{#session.LOGIN_KEY.userCode}" /></mark>
                <br>
                <s:text name="w.prefixNotifications" /> <mark>0</mark> 
                <s:text name="w.suffixNotifications" /> &nbsp;|
                <img src="./Resource/Icon/icon_fullscreen_inactive.svg" id="ButtonFullScreen" alt="PT Emobile Indonesia - MMBS, Button Full Screen"/>
                <s:url id="ButtonLogout" action="Logout" includeParams="none"/>
				<s:a href="%{ButtonLogout}" cssClass="ClearHyperlink">
               		<s:text name="b.logOut" />
               	</s:a>
            </aside>