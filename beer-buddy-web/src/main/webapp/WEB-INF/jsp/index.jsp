<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<head>
<title>Beer Buddy</title>

<script type="text/javascript">

    </script>
</head>
<body>
	<md-toolbar layout="column" class="md-default-theme">
		<!-- ng-click="openMenu()" -->
      <div class="md-toolbar-tools" flex="" layout="column" tabindex="0">

        <div layout="row" flex="">
          <button class="menu-icon" hide-gt-sm="" aria-label="Toggle Menu" style="position: relative; top: -5px;">
            <md-icon icon="img/icons/ic_menu_24px.svg"><object class="md-icon" data="img/icons/ic_menu_24px.svg"></object></md-icon>
          </button>
          <!-- ngIf: menu.currentSection.name -->
          <!-- ngIf: menu.currentPage -->
          <div style="line-height: 28px;" ng-bind="menu.currentPage || 'Beer Buddy'" class="ng-binding">Beer Buddy</div>
          <div flex=""></div>

		  <div ng-show="user.isLoggedIn">

		  	  <a class="md-button md-default-theme" ui-sref="profile" style="position: relative; padding-left: 30px; touch-action: pan-y; -webkit-user-select: none; -webkit-user-drag: none; -webkit-tap-highlight-color: rgba(0, 0, 0, 0);" tabindex="0">
	            <span class="ng-scope">Welcome, {{user.name}}</span>
	          </a>
	          <a class="md-button md-default-theme" ui-sref="logout" style="position: relative; padding-left: 30px; touch-action: pan-y; -webkit-user-select: none; -webkit-user-drag: none; -webkit-tap-highlight-color: rgba(0, 0, 0, 0);" tabindex="0">
	            <span class="ng-scope">Logout</span>
	          </a>
		  </div>
		  <div ng-show="! user.isLoggedIn">
	          <a class="md-button md-default-theme"
	          		ui-sref="login"
	          		style="position: relative; padding-left: 30px; touch-action: pan-y; -webkit-user-select: none; -webkit-user-drag: none; -webkit-tap-highlight-color: rgba(0, 0, 0, 0);" tabindex="0">
	            
	            <span class="ng-scope">Login</span>
	          </a>
		  </div>

        </div>
      </div>

    </md-toolbar>

	<div ui-view></div>

</body>
