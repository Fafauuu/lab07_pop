public interface BroadcasterWindowListener {
    void updateNewsList();
    void editNews(Integer id, String news);
    void notifyCustomers(Integer id);
}
