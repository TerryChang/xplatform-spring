var checkBrowser = {
	init: function () {
		this.browser = this.searchStr(this.browserList) || "An unknown browser";
		this.version = this.searchVer(navigator.userAgent)
			|| this.searchVer(navigator.appVersion)
			|| "an unknown version";
		this.OS = this.searchStr(this.osList) || "an unknown OS";
	},
	searchStr: function (objList) {
		for (var i=0;i<objList.length;i++)	{
			var dataStr = objList[i].string;
			var dataProp = objList[i].prop;
			this.versionStr = objList[i].versionSearch || objList[i].identity;
			if (dataStr) {
				if (dataStr.indexOf(objList[i].subString) != -1)
					return objList[i].identity;
			}
			else if (dataProp)
				return objList[i].identity;
		}
	},
	searchVer: function (str) {
		var idx = str.indexOf(this.versionStr);
		if (idx == -1) return;
		return parseFloat(str.substring(idx+this.versionStr.length+1));
	},
	browserList: [
		{ String: navigator.userAgent,
			subString: "OmniWeb",
			versionSearch: "OmniWeb/",
			identity: "OmniWeb"
		},
		{
			string: navigator.vendor,
			subString: "Apple",
			identity: "Safari"
		},
		{
			prop: window.opera,
			identity: "Opera"
		},
		{
			string: navigator.vendor,
			subString: "iCab",
			identity: "iCab"
		},
		{
			string: navigator.vendor,
			subString: "KDE",
			identity: "Konqueror"
		},
		{
			string: navigator.userAgent,
			subString: "Firefox",
			identity: "Firefox"
		},
		{
			string: navigator.vendor,
			subString: "Camino",
			identity: "Camino"
		},
		{		// for newer Netscapes (6+)
			string: navigator.userAgent,
			subString: "Netscape",
			identity: "Netscape"
		},
		{
			string: navigator.userAgent,
			subString: "MSIE",
			identity: "Explorer",
			versionSearch: "MSIE"
		},
		{
			string: navigator.userAgent,
			subString: "Gecko",
			identity: "Mozilla",
			versionSearch: "rv"
		},
		{ 		// for older Netscapes (4-)
			string: navigator.userAgent,
			subString: "Mozilla",
			identity: "Netscape",
			versionSearch: "Mozilla"
		}
	],
	osList : [
		{
			string: navigator.platform,
			subString: "Win",
			identity: "Windows"
		},
		{
			string: navigator.platform,
			subString: "Mac",
			identity: "Mac"
		},
		{
			string: navigator.platform,
			subString: "Linux",
			identity: "Linux"
		}
	]

};
checkBrowser.init();