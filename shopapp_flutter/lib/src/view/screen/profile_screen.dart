import 'dart:convert';
import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'package:shared_preferences/shared_preferences.dart';
import 'package:jwt_decoder/jwt_decoder.dart'; // Thư viện giải mã JWT

class ProfileScreen extends StatefulWidget {
  const ProfileScreen({super.key});

  @override
  _ProfileScreenState createState() => _ProfileScreenState();
}

class _ProfileScreenState extends State<ProfileScreen> {
  String fullName = '';
  String phoneNumber = '';
  String address = '';
  String dateOfBirth = '';
  bool isLoading = true;
  bool isError = false;
  int? userId;

  // Hàm lấy dữ liệu người dùng từ API
  Future<void> _fetchUserProfile() async {
    // Lấy token từ SharedPreferences
    final prefs = await SharedPreferences.getInstance();
    final token = prefs.getString('auth_token');

    if (token == null) {
      setState(() {
        isLoading = false;
        isError = true;
      });
      return;
    }

    // Giải mã JWT để lấy userId
    final decodedToken = JwtDecoder.decode(token);
    setState(() {
      userId = decodedToken['id']; // Lấy userId từ token
    });

    if (userId == null) {
      setState(() {
        isLoading = false;
        isError = true;
      });
      return;
    }

    // Gửi yêu cầu GET đến API với token và userId
    final response = await http.get(
      Uri.parse("http://localhost:8088/api/v1/users/$userId"),
      headers: {
        'Authorization': 'Bearer $token', // Gửi token trong header
      },
    );

    if (response.statusCode == 200) {
      final Map<String, dynamic> responseData = jsonDecode(response.body);

      setState(() {
        fullName = responseData['fullName'] ?? 'N/A';
        phoneNumber = responseData['phoneNumber'] ?? 'N/A';
        address = responseData['address'] ?? 'N/A';
        dateOfBirth = responseData['dateOfBirth'] ?? 'N/A';
        isLoading = false;
      });
    } else {
      setState(() {
        isLoading = false;
        isError = true;
      });
    }
  }

  @override
  void initState() {
    super.initState();
    _fetchUserProfile(); // Gọi hàm lấy thông tin người dùng
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('Profile')),
      body: isLoading
          ? const Center(child: CircularProgressIndicator()) // Hiển thị loading
          : isError
          ? const Center(child: Text('Failed to load profile.')) // Lỗi khi lấy dữ liệu
          : Padding(
        padding: const EdgeInsets.all(20.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            const SizedBox(height: 20),
            const Text(
              "Profile Information",
              style: TextStyle(fontWeight: FontWeight.bold, fontSize: 22),
            ),
            const SizedBox(height: 20),
            Text('Full Name: $fullName', style: const TextStyle(fontSize: 18)),
            const SizedBox(height: 10),
            Text('Phone Number: $phoneNumber', style: const TextStyle(fontSize: 18)),
            const SizedBox(height: 10),
            Text('Address: $address', style: const TextStyle(fontSize: 18)),
            const SizedBox(height: 10),
            Text('Date of Birth: $dateOfBirth', style: const TextStyle(fontSize: 18)),
          ],
        ),
      ),
    );
  }
}
