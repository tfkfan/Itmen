<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<link
	href="//netdna.bootstrapcdn.com/font-awesome/4.1.0/css/font-awesome.min.css"
	rel="stylesheet">
<hr>
</div>
<footer class="footer">
<div class="container">
	<div class="row">
		<div class="col-lg-12">
			<div class="col-lg-3 col-md-6">
				<div class="container">

					<div class="row">

						<div class="col-lg-3 col-md-6">
							<h3>Категории</h3>
							<ul>
								<li><a href=""><i class="fa fa-file"></i> News</a></li>
								<li><a href=""><i class="fa fa-android"></i> Android</a></li>
								<li><a href=""><i class="fa fa-code"></i> C#</a></li>
								
							</ul>
						</div>

						<div class="col-lg-3"></div>
						<div class="col-lg-3"></div>

						<div class="col-lg-3 col-md-6">
							<h3>Contact:</h3>
							<p>Have a question or feedback? Contact me!</p>
							<p>
								<a href="" title="Contact me!"><i class="fa fa-envelope"></i>
									Contact</a>
							</p>
						</div>
					</div>
					<br />
					<div class="row">
						<div id="fb-root"></div>
						<script>
							(function(d, s, id) {
								var js, fjs = d.getElementsByTagName(s)[0];
								if (d.getElementById(id))
									return;
								js = d.createElement(s);
								js.id = id;
								js.src = "//connect.facebook.net/hu_HU/sdk.js#xfbml=1&version=v2.0";
								fjs.parentNode.insertBefore(js, fjs);
							}(document, 'script', 'facebook-jssdk'));
						</script>

						<div class="fb-like" data-href="" data-layout="standard"
							data-action="like" data-show-faces="true" data-share="true"></div>

						<a href="https://twitter.com/share" class="twitter-share-button"
							data-url="">Tweet</a>
						<script>
							!function(d, s, id) {
								var js, fjs = d.getElementsByTagName(s)[0], p = /^http:/
										.test(d.location) ? 'http' : 'https';
								if (!d.getElementById(id)) {
									js = d.createElement(s);
									js.id = id;
									js.src = p
											+ '://platform.twitter.com/widgets.js';
									fjs.parentNode.insertBefore(js, fjs);
								}
							}(document, 'script', 'twitter-wjs');
						</script>

						<div class="g-plusone" data-annotation="inline" data-width="300"
							data-href=""></div>

						<!-- Helyezd el ezt a címkét az utolsó +1 gomb címke mögé. -->
						<script type="text/javascript">
							(function() {
								var po = document.createElement('script');
								po.type = 'text/javascript';
								po.async = true;
								po.src = 'https://apis.google.com/js/platform.js';
								var s = document.getElementsByTagName('script')[0];
								s.parentNode.insertBefore(po, s);
							})();
						</script>
						<br />

						<hr>
						<p>
							Copyright © Your Website | <a href="">Privacy Policy</a> | <a
								href="">Terms of Use</a>
						</p>
					</div>
</footer>
</div>
</div>
</body>
</html>