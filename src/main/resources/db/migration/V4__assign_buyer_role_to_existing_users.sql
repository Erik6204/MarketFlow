INSERT INTO user_roles (user_id, role_id)
SELECT users.id, roles.id
FROM users
JOIN roles ON roles.name = 'BUYER'
ON CONFLICT (user_id, role_id) DO NOTHING;
