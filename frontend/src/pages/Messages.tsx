import { useEffect, useState } from 'react';
import { MessageCircle, Send, Users } from 'lucide-react';
import API from '../api/api';
import type { FriendDto, MessageDto, UserProfile } from '../types';

function getInitials(name: string) {
  return name
    .split(' ')
    .map((w) => w[0])
    .join('')
    .slice(0, 2)
    .toUpperCase();
}

function formatTime(value: string) {
  return new Date(value).toLocaleString('en-US', {
    month: 'short',
    day: 'numeric',
    hour: 'numeric',
    minute: '2-digit',
  });
}

export default function Messages() {
  const [friends, setFriends] = useState<FriendDto[]>([]);
  const [selectedFriend, setSelectedFriend] = useState<UserProfile | null>(null);
  const [messages, setMessages] = useState<MessageDto[]>([]);
  const [content, setContent] = useState('');
  const [loadingFriends, setLoadingFriends] = useState(true);
  const [loadingMessages, setLoadingMessages] = useState(false);
  const [error, setError] = useState('');

  const stored = localStorage.getItem('user');
  const currentUser: UserProfile | null = stored ? JSON.parse(stored) : null;

  useEffect(() => {
    void API.get<FriendDto[]>('/connections/friends')
      .then((res) => {
        setFriends(res.data);
        if (res.data.length > 0) {
          setSelectedFriend(res.data[0].user);
        }
      })
      .catch((err) => setError(err.response?.data?.message || 'Failed to load friends'))
      .finally(() => setLoadingFriends(false));
  }, []);

  useEffect(() => {
    if (!selectedFriend?.id) return;

    setLoadingMessages(true);
    setError('');

    void API.get<MessageDto[]>(`/messages/${selectedFriend.id}`)
        .then((res) => {
          setMessages(res.data);
          window.dispatchEvent(new Event('messages-read'));
        })      .catch((err) => setError(err.response?.data?.message || 'Failed to load messages'))
      .finally(() => setLoadingMessages(false));
  }, [selectedFriend?.id]);

  const sendMessage = async (e: React.FormEvent) => {
    e.preventDefault();

    if (!selectedFriend?.id || !content.trim()) return;

    try {
      setError('');
      const res = await API.post<MessageDto>('/messages', {
        receiverId: selectedFriend.id,
        content: content.trim(),
      });

      setMessages((prev) => [...prev, res.data]);
      setContent('');
    } catch (err: any) {
      setError(err.response?.data?.message || 'Failed to send message');
    }
  };

  return (
    <div>
      <div className="page-header">
        <h1>Messages</h1>
        <p>Text your connected friends only.</p>
      </div>

      {error && <div className="alert alert-error mb-4">{error}</div>}

      <div
        className="card"
        style={{
          display: 'grid',
          gridTemplateColumns: '280px 1fr',
          minHeight: 560,
          padding: 0,
          overflow: 'hidden',
        }}
      >
        <aside style={{ borderRight: '1px solid var(--gray-200)', padding: 16 }}>
          <h3 style={{ display: 'flex', alignItems: 'center', gap: 8, fontSize: 15, marginBottom: 16 }}>
            <Users size={16} /> Friends
          </h3>

          {loadingFriends ? (
            <p className="text-sm text-muted">Loading friends...</p>
          ) : friends.length === 0 ? (
            <p className="text-sm text-muted">You do not have any friends to message yet.</p>
          ) : (
            <div style={{ display: 'flex', flexDirection: 'column', gap: 8 }}>
              {friends.map((friend) => {
                const active = friend.user.id === selectedFriend?.id;

                return (
                  <button
                    key={friend.user.id ?? friend.user.fullName}
                    className={`btn ${active ? 'btn-primary' : 'btn-secondary'}`}
                    style={{ justifyContent: 'flex-start' }}
                    onClick={() => setSelectedFriend(friend.user)}
                  >
                    {friend.user.fullName}
                  </button>
                );
              })}
            </div>
          )}
        </aside>

        <section style={{ display: 'flex', flexDirection: 'column', minHeight: 560 }}>
          {selectedFriend ? (
            <>
              <div style={{ borderBottom: '1px solid var(--gray-200)', padding: 16 }}>
                <div style={{ display: 'flex', alignItems: 'center', gap: 10 }}>
                  <div className="avatar avatar-sm">
                    {selectedFriend.profileImageUrl
                      ? <img src={selectedFriend.profileImageUrl} alt="" />
                      : getInitials(selectedFriend.fullName)}
                  </div>
                  <div>
                    <h2 style={{ fontSize: 16, fontWeight: 700 }}>{selectedFriend.fullName}</h2>
                    <p className="text-sm text-muted">Connected friend</p>
                  </div>
                </div>
              </div>

              <div style={{ flex: 1, padding: 16, overflowY: 'auto' }}>
                {loadingMessages ? (
                  <p className="text-sm text-muted">Loading messages...</p>
                ) : messages.length === 0 ? (
                  <div className="empty-state">
                    <MessageCircle size={42} />
                    <h3>No messages yet</h3>
                    <p>Start the conversation with {selectedFriend.fullName}.</p>
                  </div>
                ) : (
                  <div style={{ display: 'flex', flexDirection: 'column', gap: 12 }}>
                    {messages.map((message) => {
                      const mine = message.sender.id === currentUser?.id;

                      return (
                        <div
                          key={message.id}
                          style={{
                            display: 'flex',
                            justifyContent: mine ? 'flex-end' : 'flex-start',
                          }}
                        >
                          <div
                            style={{
                              maxWidth: '70%',
                              background: mine ? 'var(--primary)' : 'var(--gray-100)',
                              color: mine ? '#fff' : 'var(--gray-900)',
                              padding: '10px 12px',
                              borderRadius: 14,
                            }}
                          >
                            <p style={{ marginBottom: 4 }}>{message.content}</p>
                            <p
                              style={{
                                fontSize: 11,
                                opacity: 0.75,
                                textAlign: mine ? 'right' : 'left',
                              }}
                            >
                              {formatTime(message.createdAt)}
                            </p>
                          </div>
                        </div>
                      );
                    })}
                  </div>
                )}
              </div>

              <form
                onSubmit={sendMessage}
                style={{
                  borderTop: '1px solid var(--gray-200)',
                  padding: 16,
                  display: 'flex',
                  gap: 8,
                }}
              >
                <input
                  className="form-input"
                  placeholder={`Message ${selectedFriend.fullName}`}
                  value={content}
                  onChange={(e) => setContent(e.target.value)}
                />
                <button className="btn btn-primary" type="submit" disabled={!content.trim()}>
                  <Send size={16} /> Send
                </button>
              </form>
            </>
          ) : (
            <div className="empty-state" style={{ margin: 'auto' }}>
              <MessageCircle size={48} />
              <h3>Select a friend</h3>
              <p>Choose a connected friend to start messaging.</p>
            </div>
          )}
        </section>
      </div>
    </div>
  );
}
