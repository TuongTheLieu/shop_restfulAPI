import 'dart:convert'; // Để xử lý JSON
import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;

class SignupPage extends StatefulWidget {
  const SignupPage({super.key});

  @override
  State<SignupPage> createState() => _SignupPageState();
}

class _SignupPageState extends State<SignupPage> {
  final TextEditingController _nameController = TextEditingController();
  final TextEditingController _phoneController = TextEditingController();
  final TextEditingController _passwordController = TextEditingController();
  final TextEditingController _retypePasswordController = TextEditingController();
  final TextEditingController _addressController = TextEditingController();
  final TextEditingController _dobController = TextEditingController();

  // Hàm gọi API để đăng ký tài khoản
  Future<void> _signup() async {
    const String apiUrl = "http://localhost:8088/api/v1/users/register";

    // Kiểm tra mật khẩu nhập lại
    if (_passwordController.text != _retypePasswordController.text) {
      _showErrorDialog("Mật khẩu không khớp. Vui lòng thử lại.");
      return;
    }

    // Dữ liệu cần gửi đến API
    final Map<String, dynamic> data = {
      "fullname": _nameController.text,
      "phone_number": _phoneController.text,
      "password": _passwordController.text,
      "retype_password": _retypePasswordController.text,
      "address": _addressController.text,
      "date_of_birth": _dobController.text,
      "role_id": 1,
      "facebook_account_id": "",
      "google_account_id": "",
    };

    try {
      // Gửi yêu cầu POST đến API
      final response = await http.post(
        Uri.parse(apiUrl),
        headers: {"Content-Type": "application/json"},
        body: jsonEncode(data),
      );

      if (response.statusCode == 201) {
        // Thành công
        print("Đăng ký thành công");
        _showSuccessDialog("Đăng ký thành công. Hãy đăng nhập!");
      } else {
        // Thất bại: hiển thị thông báo lỗi
        final errorData = jsonDecode(response.body);
        print("Đăng ký thất bại: ${errorData['message']}");
        _showErrorDialog(errorData['message']);
      }
    } catch (e) {
      // Xử lý lỗi (ví dụ: không kết nối được server)
      print("Lỗi: $e");
      _showErrorDialog("Không thể kết nối đến server.");
    }
  }

  // Hàm hiển thị thông báo lỗi
  void _showErrorDialog(String message) {
    showDialog(
      context: context,
      builder: (ctx) => AlertDialog(
        title: const Text("Lỗi đăng ký"),
        content: Text(message),
        actions: [
          TextButton(
            onPressed: () => Navigator.of(ctx).pop(),
            child: const Text("Đóng"),
          ),
        ],
      ),
    );
  }

  // Hàm hiển thị thông báo thành công
  void _showSuccessDialog(String message) {
    showDialog(
      context: context,
      builder: (ctx) => AlertDialog(
        title: const Text("Thành công"),
        content: Text(message),
        actions: [
          TextButton(
            onPressed: () {
              Navigator.of(ctx).pop();
              Navigator.of(context).pushReplacementNamed('/');
            },
            child: const Text("Đồng ý"),
          ),
        ],
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    final Size size = MediaQuery.of(context).size;
    return Scaffold(
      body: SingleChildScrollView(
        child: Container(
          width: size.width,
          height: size.height,
          padding:
          const EdgeInsets.only(left: 20, right: 20, top: 70, bottom: 80),
          color: Colors.white,
          child: Column(
            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Text(
                'Create an Account',
                style: Theme.of(context).textTheme.headline1,
              ),
              Column(
                crossAxisAlignment: CrossAxisAlignment.end,
                children: [
                  const SizedBox(height: 20),
                  _buildTextField(_nameController, 'Full Name'),
                  const SizedBox(height: 10),
                  _buildTextField(_phoneController, 'Phone Number'),
                  const SizedBox(height: 10),
                  _buildTextField(_addressController, 'Address'),
                  const SizedBox(height: 10),
                  _buildTextField(_dobController, 'Date of Birth (YYYY-MM-DD)'),
                  const SizedBox(height: 10),
                  _buildPasswordField(_passwordController, 'Password'),
                  const SizedBox(height: 10),
                  _buildPasswordField(_retypePasswordController, 'Retype Password'),
                ],
              ),
              Column(
                children: [
                  ElevatedButton(
                    onPressed: _signup, // Gọi hàm đăng ký khi nhấn nút
                    style: ElevatedButton.styleFrom(
                      backgroundColor: Colors.white,
                      padding: const EdgeInsets.all(18),
                      elevation: 0,
                      shape: RoundedRectangleBorder(
                        borderRadius: BorderRadius.circular(20),
                      ),
                    ),
                    child: const Center(child: Text('Sign Up')),
                  ),
                  const SizedBox(height: 20),
                  TextButton(
                    onPressed: () {
                      Navigator.of(context).pushReplacementNamed('/');
                    },
                    child: const Text('Already have an account? Login'),
                  ),
                ],
              ),
            ],
          ),
        ),
      ),
    );
  }

  Widget _buildTextField(TextEditingController controller, String hintText) {
    return Container(
      padding: const EdgeInsets.symmetric(horizontal: 20, vertical: 5),
      decoration: BoxDecoration(
        color: Theme.of(context).primaryColorLight,
        borderRadius: const BorderRadius.all(Radius.circular(20)),
      ),
      child: TextField(
        controller: controller,
        decoration: InputDecoration(
          border: InputBorder.none,
          hintText: hintText,
        ),
      ),
    );
  }

  Widget _buildPasswordField(TextEditingController controller, String hintText) {
    return Container(
      padding: const EdgeInsets.symmetric(horizontal: 20, vertical: 5),
      decoration: BoxDecoration(
        color: Theme.of(context).primaryColorLight,
        borderRadius: const BorderRadius.all(Radius.circular(20)),
      ),
      child: TextField(
        controller: controller,
        obscureText: true,
        decoration: InputDecoration(
          border: InputBorder.none,
          hintText: hintText,
        ),
      ),
    );
  }
}
