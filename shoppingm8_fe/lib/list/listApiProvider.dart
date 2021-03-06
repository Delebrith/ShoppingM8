import 'package:dio/dio.dart';
import 'package:shoppingm8_fe/common/ApiProvider.dart';
import 'package:shoppingm8_fe/list/dto/listRequestDto.dart';
import 'package:shoppingm8_fe/list/invitation/dto/ListInvitationRequestDto.dart';

import '../main.dart';

class ListApiProvider extends ApiProvider {
  static final String listPrefix = "/list/";

  Uri uri;

  ListApiProvider() : super() {
    uri = Uri.parse(serverUrl + listPrefix);
  }

  Future<Response> getMyLists() {
    return sendGetRequest(endpoint: "my-lists");
  }

  Future<Response> getSingleList(num id) {
    return sendGetRequest(endpoint: id.toString());
  }

  Future<Response> createList(ListRequestDto dto) {
    return sendPostRequest(endpoint: "", body: dto);
  }

  Future<Response> updateList(num id, ListRequestDto dto) {
    return sendPatchRequest(endpoint: id.toString(), body: dto);
  }

  Future<Response> deleteList(num id) {
    return sendDeleteRequest(endpoint: id.toString());
  }

  Future<Response> leaveList(num id) {
    return sendDeleteRequest(endpoint: id.toString() + "/leave");
  }

  Future<Response> inviteUser(num id, ListInvitationRequestDto requestDto) {
    return sendPostRequest(endpoint: id.toString() + "/invitation", body: requestDto);
  }

  Future<Response> getInvitationsByList(num id) {
    return sendGetRequest(endpoint: id.toString() + "/invitation");
  }
}