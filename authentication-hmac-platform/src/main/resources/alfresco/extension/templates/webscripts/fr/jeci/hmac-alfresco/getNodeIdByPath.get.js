var path = args.path;

if (path) {
  var node = companyhome.childByNamePath(path);
  if (node) {
    model.node = node;
  } else {
    status.setCode(status.STATUS_NOT_FOUND, "The path " + path + " does not exist.");
  }
} else {
  status.setCode(status.STATUS_BAD_REQUEST, "Path missing");
}
