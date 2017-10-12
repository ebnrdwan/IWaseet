package ash.wasset.serverconnection;

public class Url {

	private static Url url = new Url();

	public static Url getInstance( ) {
		return url;
	}

	//http://services.iwaseet.net/


	final public String clientRegistrationURL="http://services.iwaseet.net/api/User/register";
	final public String serviceProviderRegistrationURL="http://services.iwaseet.net/api/User/register";
	final public String loginURL="http://services.iwaseet.net/api/User/Login";
	final public String activationURL="http://services.iwaseet.net/api/SPUser/Activation";
	final public String categoriesURL="http://services.iwaseet.net/ategoriesList";
	final public String addCategoriesURL="http://services.iwaseet.net/api/SPUser/AddCategories";
	final public String removeCategoriesURL="http://services.iwaseet.net/api/SPUser/RemoveCategories";
	final public String getSelectedcategoriesURL="http://services.iwaseet.net/api/SPUser/GetCategories";
	final public String categoriesImageURL="http://services.iwaseet.net/Uploads/Categories";
	final public String socialMediasImageURL="http://services.iwaseet.net/Uploads/SocialMedias";
	final public String profilePicturesURL="http://services.iwaseet.net/Uploads/ProfilePictures/";
	final public String newsPicturesURL="http://services.iwaseet.net/Uploads/News/";
	final public String serviceListURL="http://services.iwaseet.net/api/User/GetServiceListSummary";
	final public String searchServiceListSummaryURL="http://services.iwaseet.net/api/User/SearchServiceListSummary";
	final public String callMeURL="http://services.iwaseet.net/api/User/CallMe";
	final public String sendRequestURL="http://services.iwaseet.net/api/User/SendLocation";
	final public String sendComplainsURL="http://services.iwaseet.net/api/User/SendComplains";
	final public String addReviewURL="http://services.iwaseet.net/api/User/AddReview";
	final public String getServiceProviderReviewsURL="http://services.iwaseet.net/api/ClientUser/GetServiceProviderReviews";
	final public String updateUserProfileURL="http://services.iwaseet.net/api/User/updateUserProfile";
	final public String updateUserProfilePicURL="http://services.iwaseet.net/api/User/UpdateUserProfilePic";
	final public String updateUserProfilePasswordURL="http://services.iwaseet.net/api/User/updateUserProfilePassword";
	final public String workHoursURL="http://services.iwaseet.net/api/ClientUser/GetServiceProviderHourWorks";
	final public String showLocationRequestsURL="http://services.iwaseet.net/api/SPUser/ShowLocationRequests";
	final public String ShowCallRequeststsURL="http://services.iwaseet.net/api/SPUser/ShowCallRequests";
	final public String showClientReviewsURL="http://services.iwaseet.net/api/SPUser/ShowClientReview";
	final public String getWorkHoursURL="http://services.iwaseet.net/api/SPUser/GetWorkHours";
	final public String updateWorkHoursURL="http://services.iwaseet.net/api/SPUser/UpdateWorkHours";
	final public String getPriceListURL="http://services.iwaseet.net/api/SPUser/ShowPriceList";
	final public String getNewsURL="http://services.iwaseet.net/api/SPUser/ShowNews";
	final public String searchGetCategoriesURL="http://services.iwaseet.net/api/ClientUser/SearchGetCategories";
	final public String getServiceProviderByidURL="http://services.iwaseet.net/api/User/GetServiceProviderByid";
	final public String socialMediaURL="http://services.iwaseet.net/api/User/GetSpUserSocialMedias";
	final public String getsocialMediaURL="http://services.iwaseet.net/api/SPUser/GetSpUserSocialMedias";
	final public String getAllSocialMediaURL="http://services.iwaseet.net/api/SPUser/ShowSocialMedia";
	final public String addSocialMediaURL="http://services.iwaseet.net/api/SPUser/AddSpUserSocialMedia";
	final public String deleteSocialMediaURL="http://services.iwaseet.net/api/SPUser/DeleteSpUserSocialMedia";
	final public String updateSocialMediaURL="http://services.iwaseet.net/api/SPUser/UpdateSpUserSocialMedia";
}